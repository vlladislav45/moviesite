package com.filmi3k.movies.models.binding;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserInfoBindingModel {
    private int userId;
    private String firstName;
    private String lastName;
    private String gender;

    public UserInfoBindingModel() {
        firstName = "";
        lastName = "";
        gender = "";
    }
}
