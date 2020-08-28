package com.jmovies.domain.models.binding;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BookmarkBindingModel {
    private int userId;
    private int movieId;
}
