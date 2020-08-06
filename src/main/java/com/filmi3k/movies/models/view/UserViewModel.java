package com.filmi3k.movies.models.view;

import com.filmi3k.movies.domain.entities.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class UserViewModel {
    private String username;
    private String email;
    private List<UserRole> authorities = new ArrayList<>();
    private UserPreferences userPreferences;
    private UserInfo userInfo;
    private UsersRating usersRating;
    private List<String> bookmarks = new ArrayList<>();
    private Timestamp createdTime;
    private boolean isEnabled;

    public static UserViewModel toViewModel(User user) {
        UserViewModel userViewModel = new UserViewModel();
        userViewModel.setUsername(user.getUsername());
        userViewModel.setEmail(user.getEmail());
        userViewModel.setAuthorities(user.getAuthorities());
        userViewModel.setUserPreferences(user.getUserPreferences());
        userViewModel.setUserInfo(user.getUserInfo());

        List<Movie> movies = user.getBookMarks().stream().map(Bookmark::getMovie).collect(Collectors.toList());
        userViewModel.bookmarks = movies.stream().map(Movie::getMovieName).collect(Collectors.toList());

        userViewModel.setCreatedTime(Timestamp.valueOf(user.getDateTimeCreated()));
        userViewModel.setEnabled(user.isEnabled());

        return userViewModel;
    }
}
