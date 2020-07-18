package com.filmi3k.movies.services.base;

import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.domain.entities.User;
import com.filmi3k.movies.domain.entities.UsersRating;
import com.filmi3k.movies.models.binding.UserRatingBindingModel;
import com.filmi3k.movies.models.binding.UserRegisterBindingModel;
import com.filmi3k.movies.utils.FileParser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Set;

public interface UserService extends UserDetailsService {
    boolean add(UserRegisterBindingModel u);

    void delete(User u);

    Set<User> getAllUsers();

    boolean getUserByUsername(String username);

    boolean getUserByEmail(String email);

    User getById(int id);

    User ban(User user, FileParser fileParser);

    Set<User> getBannedUsers();

    User getUserByIpAddress(String ipAddress);

    boolean isEnabledUser(int id);

    void banUsersByIP(FileParser fileParser);

    UsersRating checkRating(User user, Movie movie);

    void addUserRating(User user, Movie movie, double userRating, String comment);
}
