package com.filmi3k.movies.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AuthenticationResponseBindingModel {
    private String jwt;

    public AuthenticationResponseBindingModel(String jwt) {
        this.jwt = jwt;
    }
}
