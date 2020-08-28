package com.jmovies.domain.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserChangePasswordBindingModel {
    private String newPassword;
    private String oldPassword;
}
