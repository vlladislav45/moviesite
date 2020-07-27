package com.filmi3k.movies.models.binding;

import lombok.Getter;

@Getter
public class AuthenticationResponseBindingModel {
    private final String jwt;

    public AuthenticationResponseBindingModel(String jwtA) {
        jwt = jwtA;
    }
}
