package com.filmi3k.movies.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserRatingBindingModel {
    private int userId;
    private int movieId;
    private double movieRating;
    private String comment;
}
