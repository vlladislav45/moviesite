package com.jmovies.domain.models.binding;

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
