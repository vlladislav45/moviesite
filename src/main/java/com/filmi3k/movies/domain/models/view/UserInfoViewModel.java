package com.filmi3k.movies.domain.models.view;

import com.filmi3k.movies.domain.entities.UserInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserInfoViewModel {
    private String firstName;
    private String lastName;
    private String gender;

    public static UserInfoViewModel toViewModel(UserInfo userInfo) {
        UserInfoViewModel userInfoViewModel = new UserInfoViewModel();

        userInfoViewModel.firstName = userInfo.getFirstName();
        userInfoViewModel.lastName = userInfo.getLastName();
        userInfoViewModel.gender = userInfo.getGender().getGenderName();

        return userInfoViewModel;
    }
}
