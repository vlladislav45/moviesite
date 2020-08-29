package com.jmovies.domain.models.binding;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserRatingBindingModel {
    private int userId;
    private int movieId;
    private double movieRating;
    private String comment;
}
