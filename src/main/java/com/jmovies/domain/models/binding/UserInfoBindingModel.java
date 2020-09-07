package com.jmovies.domain.models.binding;

import lombok.Data;

@Data
public class UserInfoBindingModel {
    private int userId;
    private String firstName;
    private String lastName;
    private String gender;

    public UserInfoBindingModel() {
    }
}
