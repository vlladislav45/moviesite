package com.jmovies.domain.models.view;

import com.jmovies.domain.entities.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class UserViewModel {
    private int userId;
    private String username;
    private String email;
    private Set<UserRole> authorities = new HashSet<>();
    private UserPreferences userPreferences;
    private UserInfoViewModel userInfoViewModel;
    private UsersRating usersRating;
    private List<BookmarkViewModel> bookmarks = new ArrayList<>();
    private Long createdTime;
    private boolean isEnabled;
    private List<DeviceLogViewModel> deviceLogs = new ArrayList<>();

    public static UserViewModel toViewModel(User user) {
        UserViewModel userViewModel = new UserViewModel();
        if(user != null) {
            userViewModel.setUsername(user.getUsername());
            userViewModel.setEmail(user.getEmail());
            userViewModel.setAuthorities(user.getAuthorities());
            userViewModel.setUserPreferences(user.getUserPreferences());
            userViewModel.userInfoViewModel = UserInfoViewModel.toViewModel(user.getUserInfo());
            userViewModel.setUserId(user.getUserId());

            List<Movie> movies = user.getBookMarks().stream().map(Bookmark::getMovie).collect(Collectors.toList());
            userViewModel.bookmarks = movies.stream().map(BookmarkViewModel::toViewModel).collect(Collectors.toList());

            userViewModel.setCreatedTime(Timestamp.valueOf(user.getDateTimeCreated()).getTime());
            userViewModel.setEnabled(user.isEnabled());

            userViewModel.deviceLogs = user.getDeviceLogs().stream().map(DeviceLogViewModel::toViewModel).collect(Collectors.toList());
        }

        return userViewModel;
    }
}
