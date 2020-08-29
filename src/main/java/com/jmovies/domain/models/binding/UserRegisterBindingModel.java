package com.jmovies.domain.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserRegisterBindingModel {
    private String email;

    private String username;

    private String password;

    private String confirmPassword;

    private String ipAddress;
}
