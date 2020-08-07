package com.filmi3k.movies.services.base;

import com.filmi3k.movies.domain.entities.*;
import com.filmi3k.movies.models.binding.UserInfoBindingModel;
import com.filmi3k.movies.models.binding.UserRegisterBindingModel;
import com.filmi3k.movies.utils.FileParser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Set;

public interface UserService extends UserDetailsService {
    boolean add(UserRegisterBindingModel u);

    void addProfilePicture(String data, String username);

    void changeUserInfo(UserInfoBindingModel userInfoModel);

    boolean isBookmarkFound(User user, Movie movie);

    void addBookmark(User user, Movie movie);

    void deleteBookmark(User user, Movie movie);

    void delete(User u);

    Set<User> getAllUsers();

    boolean checkUsernameAvailable(String username);

    User getByUsername(String username);

    boolean isEmailAvailable(String email);

    User getById(int id);

    User ban(User user, FileParser fileParser);

    Set<User> getBannedUsers();

    User getUserByIpAddress(String ipAddress);

    boolean isEnabledUser(int id);

    void banUsersByIP(FileParser fileParser);

    UsersRating checkRating(User user, Movie movie);

    void addUserRating(User user, Movie movie, double userRating, String comment);

    void changeUserTheme(int userId, String theme);

    String getSelectedTheme(int userId);
}
