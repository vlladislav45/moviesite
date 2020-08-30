package com.jmovies.services.impl;

import com.jmovies.domain.models.binding.UserInfoBindingModel;
import com.jmovies.domain.models.binding.UserRegisterBindingModel;
import com.jmovies.services.base.UserService;
import com.jmovies.utils.FileParser;
import com.jmovies.domain.entities.*;
import com.jmovies.repository.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final UserImageRepository userImageRepository;
    private final UserPreferencesRepository userPreferencesRepository;
    private final UserInfoRepository userInfoRepository;
    private final UsersRatingRepository usersRatingRepository;
    private final GenderRepository genderRepository;
    private final BookmarkRepository bookmarkRepository;
    private final DeviceLogRepository deviceLogRepository;
    private final PasswordTokenRepository passwordTokenRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper map, BCryptPasswordEncoder bCryptPasswordEncoder,
                           RoleRepository roleRepository, UserImageRepository userImageRepository,
                           UserPreferencesRepository userPreferencesRepository, UserInfoRepository userInfoRepository,
                           UsersRatingRepository usersRatingRepository, GenderRepository genderRepository,
                           BookmarkRepository bookmarkRepository, DeviceLogRepository deviceLogRepository, PasswordTokenRepository passwordTokenRepository) {
        this.userRepository = userRepository;
        this.modelMapper = map;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
        this.userImageRepository = userImageRepository;
        this.userPreferencesRepository = userPreferencesRepository;
        this.userInfoRepository = userInfoRepository;
        this.usersRatingRepository = usersRatingRepository;
        this.genderRepository = genderRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.deviceLogRepository = deviceLogRepository;
        this.passwordTokenRepository = passwordTokenRepository;
    }

    @Override
    public boolean add(UserRegisterBindingModel userRegisterBindingModel) {
        User userEntity = this.modelMapper.map(userRegisterBindingModel, User.class);
        userEntity.setCreatedTime(Timestamp.valueOf(userEntity.getDateTimeCreated()));

        userEntity.setPassword(this.bCryptPasswordEncoder.encode(userEntity.getPassword()));

        //rules
        userEntity.setAccountNonExpired(true);
        userEntity.setAccountNonLocked(true);
        userEntity.setCredentialsNonExpired(true);
        userEntity.setEnabled(true);

        if(this.userRepository.findAll().isEmpty()) {
            Set<UserRole> roles = new HashSet<>(Set.of(roleRepository.getUserRoleByAuthority("USER"), roleRepository.getUserRoleByAuthority("ADMIN")));
            userEntity.setAuthorities(roles);
        }else {
            userEntity.setAuthorities(Set.of(roleRepository.getUserRoleByAuthority("USER")));
        }

        this.userRepository.saveAndFlush(userEntity);

        this.userInfoRepository.saveAndFlush(new UserInfo(userEntity)); //Create the relationship between User and User Info
        this.userPreferencesRepository.saveAndFlush(new UserPreferences(userEntity)); // Create the relationship between User and User Preferences

        return true;
    }

    @Override
    public Map<String, String> checkRegisterUser(UserRegisterBindingModel userRegisterBindingModel) {
        Map<String, String> errors = new HashMap<>();
        //Check user
        if(this.checkUsernameAvailable(userRegisterBindingModel.getUsername())) {
            errors.put("email", "* This user is already registered");
        }
        if(this.isEmailAvailable(userRegisterBindingModel.getEmail()))
            errors.put("email", "* This email is not available");
        if(userRegisterBindingModel.getPassword().length() < 8) {
            errors.put("password", "* The password have been more than 8 symbols");
        }
        if(!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            errors.put("password", "* The password does not match the confirmation password");
        }
        return errors;
    }

    @Override
    public void addProfilePicture(String data, String username) {
        UserInfo userInfo = userRepository.getUserByUsername(username).getUserInfo();
        this.userImageRepository.saveAndFlush(new UserImage(data, userInfo));
    }

    @Override
    public void changeUserInfo(UserInfoBindingModel userInfoModel) {
        UserInfo userInfo = this.getById(userInfoModel.getUserId()).getUserInfo();

        if(userInfoModel.getFirstName() != null)
            userInfo.setFirstName(userInfoModel.getFirstName());
        if(userInfoModel.getLastName() != null)
            userInfo.setLastName(userInfoModel.getLastName());
        if(userInfoModel.getGender() != null) {
            if(userInfoModel.getGender().equals("male")
                    || userInfoModel.getGender().equals("female"))
            userInfo.setGender(genderRepository.findByGenderName(userInfoModel.getGender()));
        }
        userInfoRepository.saveAndFlush(userInfo);
    }

    @Override
    public boolean isBookmarkFound(User user, Movie movie) {
        return bookmarkRepository.getBookmarkByUserAndMovie(user, movie) != null;
    }

    @Override
    public void addBookmark(User user, Movie movie) {
        bookmarkRepository.saveAndFlush(new Bookmark(user,movie));
    }

    @Override
    public void deleteBookmark(User user, Movie movie) {
        Bookmark bookmark = bookmarkRepository.getBookmarkByUserAndMovie(user, movie);
        bookmarkRepository.delete(bookmark);
    }

    @Override
    public void delete(User u) {
        userRepository.delete(u);
    }

    @Override
    public Set<User> getAllUsers() {
        return new HashSet<>(this.userRepository.findAll());
    }

    @Override
    public boolean checkUsernameAvailable(String username) {
        User user = userRepository.getUserByUsername(username);
        return user != null;
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    @Override
    public boolean isEmailAvailable(String email) {
        User user = userRepository.getUserByEmail(email);
        return user != null;
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public boolean checkIfValidOldPassword(User user, String password) {
        User u = userRepository.getUserByUsername(user.getUsername());
        return u != null && bCryptPasswordEncoder.matches(password, user.getPassword());
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);

        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                : null;
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }

    @Override
    public User getUserByPasswordResetToken(String resetToken) {
        return passwordTokenRepository.findByToken(resetToken).getUser();
    }

    @Override
    public void changeUserPassword(User user, String newPassword) {
        //Do not check if the user exist
        //That's already done in checkIfValidOldPassword
        User u = userRepository.getUserByUsername(user.getUsername());
        u.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.saveAndFlush(u);
    }

    @Override
    public void deleteResetToken(String resetToken) {
        PasswordResetToken passwordResetToken =  passwordTokenRepository.findByToken(resetToken);
        passwordTokenRepository.delete(passwordResetToken);
    }

    @Override
    public User getById(int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User foundUser = this.userRepository.getUserByUsername(s);
        return foundUser;
    }


    @Override
    public User ban(User user, FileParser fileParser) {
        user.setEnabled(false);
        userRepository.save(user);

        List<String> ipAddresses = user.getDeviceLogs().stream().map(DeviceLog::getIpAddress).collect(Collectors.toList());

        if(fileParser != null) fileParser.addBannedIPAddress(ipAddresses); // To whitelist

        if(!userRepository.isEnabledUser(user.getUserId())) {
            return user;
        }
        return null;
    }

    @Override
    public Set<User> getBannedUsers() {
        return userRepository.getAllByIsEnabled(false);
    }

    @Override
    public boolean isEnabledUser(int id) {
        return userRepository.isEnabledUser(id);
    }

    @Override
    public void banUsersByIP(FileParser fileParser) {
        List<String> bannedIPs = new ArrayList<>();
        try {
            if(fileParser != null) fileParser.parseBannedIPAddresses(bannedIPs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for(String ip : bannedIPs) {
            if(deviceLogRepository.findByIpAddress(ip) != null) {
                User user = (User) deviceLogRepository.findByIpAddress(ip).getUser();

                if (userRepository.isEnabledUser(user.getUserId())){
                    this.ban(user, fileParser);
                }
            }
        }
    }

    @Override
    public UsersRating checkRating(User user, Movie movie) {
        if(usersRatingRepository.findUsersRatingByUserAndMovie(user, movie) != null)
            return usersRatingRepository.findUsersRatingByUserAndMovie(user, movie);
        return null;
    }

    @Override
    public void addUserRating(User user, Movie movie, double userRating, String comment) {
        usersRatingRepository.saveAndFlush(new UsersRating(user, movie, userRating, comment));
    }

    @Override
    public void changeUserTheme(int userId, String theme) {
        User user = this.getById(userId);
        UserPreferences userPreferences = userPreferencesRepository.getUserPreferencesByUser(user);
        if(user != null && userPreferences != null) {
            userPreferences.setSelectedTheme(theme);
            userPreferencesRepository.saveAndFlush(userPreferences);
        }
    }

    @Override
    public Page<UsersRating> findAllReviewsByUser(User user, int page, int size) {
        return usersRatingRepository.findAllByUser(user, PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdTime"))));
    }
}
