package com.filmi3k.movies.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserChangePasswordBindingModel {
    private String newPassword;
    private String oldPassword;
}
