package com.jmovies.domain.models.binding;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ResetPasswordBindingModel {
    private String token;
    private String newPassword;
}
