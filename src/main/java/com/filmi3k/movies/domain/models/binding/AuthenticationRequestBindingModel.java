package com.filmi3k.movies.domain.models.binding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthenticationRequestBindingModel {
    private String username;
    private String password;
}
