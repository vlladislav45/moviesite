package com.filmi3k.movies.domain.models.view;

import com.filmi3k.movies.domain.entities.UserImage;
import com.filmi3k.movies.domain.entities.UserInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class UserInfoViewModel {
    private String firstName = "";
    private String lastName = "";
    private String gender = "";
    private List<UserImage> userImages;

    public static UserInfoViewModel toViewModel(UserInfo userInfo) {
        UserInfoViewModel userInfoViewModel = new UserInfoViewModel();

        if(userInfo.getFirstName() != null)
            userInfoViewModel.firstName = userInfo.getFirstName();
        if(userInfo.getLastName() != null)
            userInfoViewModel.lastName = userInfo.getLastName();
        if(userInfo.getGender() != null)
            userInfoViewModel.gender = userInfo.getGender().getGenderName();
        if(userInfo.getUserImages().size() > 0)
            userInfoViewModel.userImages = userInfo.getUserImages();

        return userInfoViewModel;
    }
}
