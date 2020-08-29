package com.jmovies.domain.models.binding;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RequestAuthorFormBindingModel {
    private String fullName;
    private String email;
    private String subject; // Movie name
    private String message;
}
