package com.filmi3k.movies.models.view;

import com.filmi3k.movies.domain.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserViewModel {
    private String username;
    private String pictureName;

//    public static UserViewModel toViewModel(User user) {
//        UserViewModel userViewModel = new UserViewModel();
//        userViewModel.setUsername(user.getUsername());
//        userViewModel.setPictureName(user.get());
//    }
}
