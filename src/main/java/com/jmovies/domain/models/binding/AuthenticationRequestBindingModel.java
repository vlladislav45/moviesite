package com.jmovies.domain.models.binding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthenticationRequestBindingModel {
    private String username;
    private String password;
    private String ip;
    private String loc;
    private String uag; // User agent
}
