package com.filmi3k.movies.controllers;

import com.filmi3k.movies.domain.entities.DeviceLog;
import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.domain.entities.User;
import com.filmi3k.movies.domain.models.binding.*;
import com.filmi3k.movies.domain.models.view.MovieRatingViewModel;
import com.filmi3k.movies.domain.models.view.UserRatingViewModel;
import com.filmi3k.movies.domain.models.view.UserViewModel;
import com.filmi3k.movies.services.base.DeviceLogService;
import com.filmi3k.movies.services.base.MovieService;
import com.filmi3k.movies.services.base.StorageService;
import com.filmi3k.movies.services.base.UserService;
import com.filmi3k.movies.utils.JSONparser;
import com.filmi3k.movies.utils.JwtUtil;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.filmi3k.movies.config.Config.*;

@RestController
public class UserController {
    private final UserService userService;
    private final MovieService movieService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtTokenUtil;
    private final StorageService storageService;
    private final DeviceLogService deviceLogService;

    @Autowired
    public UserController(UserService userService, MovieService movieService, AuthenticationManager authenticationManager, JwtUtil jwtTokenUtil, StorageService storageService, DeviceLogService deviceLogService) {
        this.userService = userService;
        this.movieService = movieService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.storageService = storageService;
        this.deviceLogService = deviceLogService;
    }

    @PostMapping("/register_user")
    public ResponseEntity<Map<String,Object>> register(@RequestBody UserRegisterBindingModel userRegisterBindingModel) {
        Map<String,Object> response = new HashMap<>();
        if(!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            response.put("error", "The password does not match the confirmation password");
            return ResponseEntity.ok().body(response);
        }else {
            if(userService.checkUsernameAvailable(userRegisterBindingModel.getUsername()) || userService.isEmailAvailable(userRegisterBindingModel.getEmail())) {
                response.put("error", "This user is already registered");
                return ResponseEntity.ok().body(response);
            }
            if(userRegisterBindingModel.getPassword().length() < 8) {
                response.put("error", "The password have been more than 8 symbols");
                return ResponseEntity.ok().body(response);
            }
            this.userService.add(userRegisterBindingModel);
        }
        response.put("success", "Your successfully register an account");
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticationToken(@RequestBody AuthenticationRequestBindingModel authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        }catch (BadCredentialsException badCredentialsException) {
            return ResponseEntity.ok().body(Map.of("error", "Bad credentials"));
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

    @GetMapping("/user/{username}/{pictureName}")
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

    @GetMapping("/user/isRated")
    public ResponseEntity<Map<String,Object>> isRated(@RequestParam int userId, @RequestParam int movieId) {
        Map<String,Object> response = new HashMap<>();
        if (userService.getById(userId) != null && movieService.findById(movieId) != null) { //check if the user already exists at the system
            User user = userService.getById(userId);
            Movie movie = movieService.findById(movieId);

            if (userService.checkRating(user, movie) != null) { // check if the user has voted for the movie
                response.put("userRating",JSONparser.toJson(MovieRatingViewModel.toViewModel(userService.checkRating(user, movie)).getMovieRating()));
                response.put("comment", MovieRatingViewModel.toViewModel(userService.checkRating(user, movie)).getComment());
                return ResponseEntity.ok().body(response);
            }
        }
        response.put("error", "null");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/user/userInfo/reviewsByAuthor")
    public ResponseEntity<?> getReviewsByAuthor(@RequestParam int userId) {
        User user = userService.getById(userId);
        Set<UserRatingViewModel> userRatingViewModels = user.getUsersRatings().stream().map(UserRatingViewModel::toViewModel).collect(Collectors.toSet());

        String resp = "[";
        String moviesJson = userRatingViewModels.stream().map(JSONparser::toJson).collect(Collectors.joining(","));
        resp += moviesJson + "]";

        return ResponseEntity.ok(resp);
    }

    @PostMapping("/user/userPreferences/theme")
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

    @PostMapping("/user/security/updatePassword")
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

    @PostMapping("/user/security/deleteAccount")
    public ResponseEntity<?> deleteAccount() {
        //Get user by token username
        User user = userService.getByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName());

        if(user != null) {
            userService.delete(user);
            return ResponseEntity.ok().body(Map.of("success", "Account is deleted successfully"));
        }
        return ResponseEntity.ok().body(Map.of("error", "This user is invalid"));
    }

    @PostMapping("/user/security/deleteDeviceLog")
    public ResponseEntity<?> deleteDeviceLog(@RequestBody IpAddressBindingModel ipAddressModel, HttpServletRequest httpServletRequest) {
        //Get user by token username
        User user = userService.getByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName());

        /***
         * Check if the user exist and there ip's are from him
         */
        if(user != null && deviceLogService.findBypAddress(ipAddressModel.getIpAddress()) != null) {
            DeviceLog deviceLog = deviceLogService.findByUserAndIpAddress(user,ipAddressModel.getIpAddress());
            
            deviceLogService.delete(deviceLog);
            return ResponseEntity.ok().body(Map.of("success", "Device log is deleted successfully"));
        }
        return ResponseEntity.ok().body(Map.of("error", "This device log cannot be deleted"));
    }
}
