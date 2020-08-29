package com.jmovies.controllers;

import com.jmovies.domain.entities.*;
import com.jmovies.domain.models.binding.*;
import com.jmovies.domain.models.view.*;
import com.jmovies.services.base.*;
import com.jmovies.utils.JSONparser;
import com.jmovies.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import static com.jmovies.config.Config.*;

@RestController
public class UserController {
    private final UserService userService;
    private final MovieService movieService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtTokenUtil;
    private final StorageService storageService;
    private final DeviceLogService deviceLogService;
    private final EmailService emailService;

    @Autowired
    public UserController(UserService userService, MovieService movieService, AuthenticationManager authenticationManager, JwtUtil jwtTokenUtil, StorageService storageService, DeviceLogService deviceLogService, EmailService emailService) {
        this.userService = userService;
        this.movieService = movieService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.storageService = storageService;
        this.deviceLogService = deviceLogService;
        this.emailService = emailService;
    }

    @PostMapping("/register_user")
    public ResponseEntity<?> register(@RequestBody UserRegisterBindingModel userRegisterBindingModel) {
        Map<String,String> errors = userService.checkRegisterUser(userRegisterBindingModel);
        if(errors.size() > 0) { // If has errors
            return ResponseEntity.ok().body(errors);
        }
        userService.add(userRegisterBindingModel);
        return ResponseEntity.ok().body(Map.of("success", "You successfully registered an account"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticationToken(@RequestBody AuthenticationRequestBindingModel authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        }catch (BadCredentialsException badCredentialsException) {
            return ResponseEntity.ok().body(Map.of("error", "Bad credentials"));
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(Map.of("error", "WTF?!"));
        }

        final UserDetails userDetails = userService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);
        //Set Device log
        deviceLogService.addNewDeviceLog(authenticationRequest, jwt, userDetails);

        return ResponseEntity.ok().body(new AuthenticationResponseBindingModel(jwt));
    }

    @GetMapping("/user_me")
    public ResponseEntity<?> getUser() {
        User user = userService.getByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName());

        UserViewModel userViewModel = UserViewModel.toViewModel(user);
        return ResponseEntity.ok().body(Map.of("user", userViewModel));
    }

    /**
     * @param username the username of the user
     * @return only relevant data about the user (id, image, name, maybe activity and authority)
     */
    @GetMapping("/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        User u = this.userService.getByUsername(username);
        if (u == null)
            return ResponseEntity.ok(Map.of("error", "No such user"));
        return ResponseEntity.ok(SingleUserViewModel.toViewModel(u));
    }

    @GetMapping("/register/userAvailable/{username}")
    public ResponseEntity<Map<String,Object>> getUsernameIfExist(@PathVariable String username) {
        Map<String,Object> response = new HashMap<>();
        if(userService.checkUsernameAvailable(username)) {
            response.put("isUsernameExist", "true");
            return ResponseEntity.ok().body(response);
        }
        response.put("isUsernameExist", "false");
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/user/uploadProfilePicture")
    public ResponseEntity<Object> uploadUserPicture(@RequestParam("file") MultipartFile file) {
        System.out.println(file.getName());
        String username = file.getOriginalFilename().split("_")[2].split("\\.")[0];
        System.out.println(username);

        String path = "static/profile-picture/" + username;
        boolean isSuccess = this.storageService.store(file ,path);
        if (!isSuccess)
            return ResponseEntity.ok(new HashMap<String, String>() {{
                put("error", "Could not write your file");
            }});

        this.userService.addProfilePicture(file.getOriginalFilename(), username);
        return ResponseEntity.ok(new HashMap<String, String>() {{
            put("success", "File upload successfully");
        }});
    }

    @GetMapping("/user/picture/{username}/{pictureName}")
    public ResponseEntity<byte[]> getUserPicture(@PathVariable String username, @PathVariable String pictureName) throws IOException {
        URL url = getClass().getResource(BASE_DIR + "/profile-picture/" + username + "/" + pictureName);
        File imagePoster = new File(url.getFile());
        byte[] fileContent = Files.readAllBytes(imagePoster.toPath());

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(MediaType.IMAGE_JPEG_VALUE))
                .body(fileContent);
    }

    @PostMapping("user/userInfo")
    public ResponseEntity<?> setUserInfo(@RequestBody UserInfoBindingModel userInfoModel) {
        this.userService.changeUserInfo(userInfoModel);

        return ResponseEntity.ok().body(Map.of("success", "User info upload sucessfully"));
    }

    @PostMapping("user/userInfo/bookmark")
    public ResponseEntity<?> setBookMark(@RequestBody BookmarkBindingModel bookmarkModel) {
        User user = userService.getById(bookmarkModel.getUserId());
        Movie movie = movieService.findById(bookmarkModel.getMovieId());
        if(!userService.isBookmarkFound(user, movie)) {
            this.userService.addBookmark(user, movie);
            return ResponseEntity.ok().body(Map.of("success", "Bookmark is saved successfully"));
        }
        this.userService.deleteBookmark(user, movie);
        return ResponseEntity.ok().body(Map.of("success", "Bookmark is deleted successfully"));
    }

    /***
     * @param userId
     * @param movieId
     * @return
     */
    @GetMapping("/user/isRated")
    public ResponseEntity<Map<String,Object>> isRated(@RequestParam int userId, @RequestParam int movieId) {
        Map<String,Object> response = new HashMap<>();
        if (userService.getById(userId) != null && movieService.findById(movieId) != null) { //check if the user already exists at the system
            User user = userService.getById(userId);
            Movie movie = movieService.findById(movieId);

            if (userService.checkRating(user, movie) != null) { // check if the user has voted for the movie
                response.put("userRating",JSONparser.toJson(MovieRatingViewModel.toViewModel(userService.checkRating(user, movie)).getUserRating()));
                response.put("comment", MovieRatingViewModel.toViewModel(userService.checkRating(user, movie)).getComment());
                return ResponseEntity.ok().body(response);
            }
        }
        response.put("error", "null");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/user/userInfo/reviewsByAuthor/count")
    public ResponseEntity<?> countReviewsByAuthor(@RequestParam int userId, @RequestParam int page, @RequestParam int size) {
        User user = userService.getById(userId);
        Page<UsersRating> ratingPage = userService.findAllReviewsByUser(user, page, size);

        //If has a rating
        if(ratingPage.getContent().size() > 0) {
            return ResponseEntity.ok(Map.of("ratingCount", ratingPage.getTotalElements()));
        }
        return ResponseEntity.ok(Map.of("error", "No such ratings"));
    }

    @GetMapping("/user/userInfo/reviewsByAuthor")
    public ResponseEntity<?> getReviewsByAuthor(@RequestParam int userId, @RequestParam int page, @RequestParam int size) {
        User user = userService.getById(userId);
        Page<UsersRating> ratingPage = userService.findAllReviewsByUser(user, page, size);
        List<UserRatingViewModel> userRatingViewModels = ratingPage.getContent().stream().map(UserRatingViewModel::toViewModel).collect(Collectors.toList());

        String resp = "[";
        String userRatings = userRatingViewModels.stream().map(JSONparser::toJson).collect(Collectors.joining(","));
        resp += userRatings + "]";

        return ResponseEntity.ok(resp);
    }

    @PostMapping("/settings/userPreferences/theme")
    public ResponseEntity<?> selectTheme(@RequestBody UserSelectedThemeBindingModel userSelectedThemeModel) {
        //Get user by token username
        User user = userService.getByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName());

        int userId = user.getUserId();
        String theme = userSelectedThemeModel.getSelectedTheme();
        if(theme.equals(BASE_THEME) || theme.equals(DARK_THEME)) {
            userService.changeUserTheme(userId, theme);
            return ResponseEntity.ok().body(Map.of("success", theme));
        }
        return ResponseEntity.ok().body(Map.of("error", "Wrong theme"));
    }

    /**
     * This method is NOT like reset password method
     * @param userChangePasswordModel
     * @return
     */
    @PostMapping("/settings/security/updatePassword")
    public ResponseEntity<?> changeUserPassword(@RequestBody UserChangePasswordBindingModel userChangePasswordModel) {
        //Get user by token username
        User user = userService.getByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName());

        if (!userService.checkIfValidOldPassword(user, userChangePasswordModel.getOldPassword()))
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid old password"));
        if(userChangePasswordModel.getNewPassword().length() < 8) // check if new password is less than 8 symbols
            return ResponseEntity.ok().body(Map.of("error", "The password have been more than 8 symbols"));
        userService.changeUserPassword(user, userChangePasswordModel.getNewPassword());
        return ResponseEntity.ok().body(Map.of("success", "Update password successfully"));
    }

    @PostMapping("/settings/security/resetPassword")
    public ResponseEntity<?> resetPassword(HttpServletRequest request,
                                         @RequestBody EmailBindingModel emailBindingModel) throws MessagingException {
        User user = userService.getByEmail(emailBindingModel.getEmail());
        if (user == null) {
            ResponseEntity.ok().body(Map.of("error", "User not found"));
        }
        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);
        emailService.constructResetTokenEmail(request.getHeader("host"), token, user);
        return ResponseEntity.ok().body(Map.of("success", "Reset password email is sent"));
    }

    @GetMapping("/settings/security/changePassword")
    public ResponseEntity<?> showChangePasswordPage(@RequestParam String token) {
        String result = userService.validatePasswordResetToken(token);
        if(result != null) { // Invalid token, expired or null
            userService.deleteResetToken(token);
            return ResponseEntity.ok().body(Map.of("error", "&resetToken=" + result));
        } else {
            return ResponseEntity.ok().body(Map.of("success", "ResetToken: " + token));
        }
    }

    @PostMapping("/settings/security/savePassword")
    public ResponseEntity<?> savePassword(@RequestBody ResetPasswordBindingModel resetPasswordBindingModel) {

        String result = userService.validatePasswordResetToken(resetPasswordBindingModel.getToken());

        if(result != null) {
            return ResponseEntity.ok().body(Map.of("error", "&message=" + result));
        }

        User user = userService.getUserByPasswordResetToken(resetPasswordBindingModel.getToken());
        if(user != null) {
            userService.changeUserPassword(user, resetPasswordBindingModel.getNewPassword());
            //Save the new password of the user and delete the reset token
            userService.deleteResetToken(resetPasswordBindingModel.getToken());
            return ResponseEntity.ok().body(Map.of("success", "Reset password successfully"));
        } else {
            return ResponseEntity.ok().body(Map.of("error", "Invalid user"));
        }
    }

    @PostMapping("/settings/security/deleteAccount")
    public ResponseEntity<?> deleteAccount() {
        //Get user by token username
        User user = userService.getByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName());

        if(user != null) {
            //Delete all profile pictures of the user which is gonna be deleted
            if(user.getUserInfo().getUserImages().size() > 0) {
                List<UserImage> userImages = user.getUserInfo().getUserImages();
                for(UserImage userImage : userImages) {
                    URL url = getClass().getResource(BASE_DIR + "/profile-picture/" + user.getUsername() + "/" + userImage.getImageName());
                    File file = new File(url.getPath());

                    boolean successDelete = file.delete();
                    if(!successDelete) return ResponseEntity.ok().body(Map.of("error", "Profile picture wasn't deleted from the system, try again"));
                }
            }

            userService.delete(user);
            return ResponseEntity.ok().body(Map.of("success", "Account is deleted successfully"));
        }
        return ResponseEntity.ok().body(Map.of("error", "This user is invalid"));
    }

    @GetMapping("/settings/security/deviceLogs")
    public ResponseEntity<?> getDeviceLogs() {
        //Get user by token username
        User user = userService.getByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName());

        //Check if the user exists
        if(user != null) {
            List<DeviceLogViewModel> deviceLogs = deviceLogService.findAllByUser(user)
                    .stream()
                    .map(DeviceLogViewModel::toViewModel)
                    .collect(Collectors.toList());

            if(deviceLogs.size() > 0) {
                String resp = "[";
                String ratings = deviceLogs.stream().map(JSONparser::toJson).collect(Collectors.joining(","));
                resp += ratings + "]";

                return ResponseEntity.ok().body(resp);
            }
        }
        return ResponseEntity.ok().body(Map.of("error", "No such user"));
    }

    @PostMapping("/settings/security/deleteDeviceLog")
    public ResponseEntity<?> deleteDeviceLog(@RequestBody IpAddressBindingModel ipAddressModel) {
        //Get user by token username
        User user = userService.getByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName());

        //Check if the user exist and there ip's are from him
        if(user != null && deviceLogService.findBypAddress(ipAddressModel.getIpAddress()) != null) {
            DeviceLog deviceLog = deviceLogService.findByUserAndIpAddress(user,ipAddressModel.getIpAddress());
            
            deviceLogService.delete(deviceLog);
            return ResponseEntity.ok().body(Map.of("success", "Device log is deleted successfully"));
        }
        return ResponseEntity.ok().body(Map.of("error", "This device log cannot be deleted"));
    }

    @PostMapping("/user/movie/deleteMovie")
    public ResponseEntity<?> deleteMovie(@RequestParam int movieId) {
        Movie movie = movieService.findById(movieId);

        //Get user by token username
        User user = userService.getByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName());

        if(movie != null && user != null) {
            List<String> currentUserAuthorities = user.getAuthorities()
                    .stream().map(UserRole::getAuthority).collect(Collectors.toList());

            if(currentUserAuthorities.contains("ADMIN")) {
                URL url = getClass().getResource(BASE_DIR + "/posters/" + movie.getPoster().getPosterName());
                File poster = new File(url.getFile());

                boolean successDelete = poster.delete();

                if(successDelete) {
                    movieService.delete(movie);
                    return ResponseEntity.ok().body(Map.of("success", "Movie was deleted"));
                }
            }
        }
        return ResponseEntity.ok().body(Map.of("error", "Movie was not deleted, try again"));
    }
}
