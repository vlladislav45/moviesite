package com.filmi3k.movies.services.impl;

import com.filmi3k.movies.domain.entities.*;
import com.filmi3k.movies.models.binding.UserInfoBindingModel;
import com.filmi3k.movies.models.binding.UserRegisterBindingModel;
import com.filmi3k.movies.repository.api.*;
import com.filmi3k.movies.services.base.UserService;
import com.filmi3k.movies.utils.FileParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.*;

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

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper map, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository, UserImageRepository userImageRepository,
                           UserPreferencesRepository userPreferencesRepository, UserInfoRepository userInfoRepository, UsersRatingRepository usersRatingRepository, GenderRepository genderRepository, BookmarkRepository bookmarkRepository) {
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
    }

    @Override
    public boolean add(UserRegisterBindingModel userRegisterBindingModel) {
        User userEntity = this.modelMapper.map(userRegisterBindingModel, User.class);
        userEntity.setIpAddress("127.0.0.1");
        userEntity.setCreatedTime(Timestamp.valueOf(userEntity.getDateTimeCreated()));

        userEntity.setPassword(this.bCryptPasswordEncoder.encode(userEntity.getPassword()));

        //rules
        userEntity.setAccountNonExpired(true);
        userEntity.setAccountNonLocked(true);
        userEntity.setCredentialsNonExpired(true);
        userEntity.setEnabled(true);

        if(this.userRepository.findAll().isEmpty()) {
            List<UserRole> roles = new ArrayList<>();
            roles.add(roleRepository.getUserRoleByAuthority("USER"));
            roles.add(roleRepository.getUserRoleByAuthority("ADMIN"));
            userEntity.setAuthorities(roles);
        }else {
            List<UserRole> roles = new ArrayList<>();
            roles.add(roleRepository.getUserRoleByAuthority("USER"));
            userEntity.setAuthorities(roles);
        }

        this.userRepository.saveAndFlush(userEntity);

        this.userInfoRepository.saveAndFlush(new UserInfo(userEntity)); //Create the relationship between User and User Info
        this.userPreferencesRepository.saveAndFlush(new UserPreferences(userEntity)); // Create the relationship between User and User Preferences

        return true;
    }

    @Override
    public void addProfilePicture(String data, String username) {
        UserInfo userInfo = userRepository.getUserByUsername(username).getUserInfo();
        this.userImageRepository.saveAndFlush(new UserImage(data, userInfo));
    }

    @Override
    public void changeUserInfo(UserInfoBindingModel userInfoModel) {
        UserInfo userInfo = this.getById(userInfoModel.getUserId()).getUserInfo();

        if(!userInfoModel.getFirstName().isEmpty())
            userInfo.setFirstName(userInfoModel.getFirstName());
        if(!userInfoModel.getLastName().isEmpty())
            userInfo.setLastName(userInfoModel.getLastName());
        if(!userInfoModel.getGender().isEmpty() && userInfoModel.getGender().equals("male")
                || userInfoModel.getGender().equals("female"))
            userInfo.setGender(genderRepository.findByGenderName(userInfoModel.getGender()));
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

        if(fileParser != null) fileParser.addBannedIPAddress(user.getIpAddress()); // To whitelist

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
    public User getUserByIpAddress(String ipAddress) {
        return userRepository.getUserByIpAddress(ipAddress);
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
            if(userRepository.getUserByIpAddress(ip) != null) {
                User user = userRepository.getUserByIpAddress(ip);

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
    public String getSelectedTheme(int userId) {
        return userPreferencesRepository.getSelectedThemeByUserId(userId);
    }
}
