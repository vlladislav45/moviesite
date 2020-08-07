package com.filmi3k.movies.models.binding;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserSelectedThemeBindingModel {
    private int userId;
    private String selectedTheme;
}
