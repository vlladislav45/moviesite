package com.filmi3k.movies.domain.models.view;

import com.filmi3k.movies.domain.entities.User;
import com.filmi3k.movies.domain.entities.UserImage;
import com.filmi3k.movies.domain.entities.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class SingleUserViewModel {
    private int userId;
    private String profileImage;
    private String username;
    private List<String> authorities;

    public static SingleUserViewModel toViewModel(User user) {
        SingleUserViewModel singleUserViewModel = new SingleUserViewModel();
        singleUserViewModel.userId = user.getUserId();
        UserImage userImage = user.getUserInfo().getUserImages().get(user.getUserInfo().getUserImages().size() - 1);
        singleUserViewModel.profileImage = userImage.getImageName();
        singleUserViewModel.username = user.getUsername();
        singleUserViewModel.authorities = user.getAuthorities().stream().map(UserRole::getAuthority).collect(Collectors.toList());

        return singleUserViewModel;
    }
}
