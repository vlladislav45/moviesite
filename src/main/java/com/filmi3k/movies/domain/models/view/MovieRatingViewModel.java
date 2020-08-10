package com.filmi3k.movies.domain.models.view;

import com.filmi3k.movies.domain.entities.UsersRating;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MovieRatingViewModel {
    private double movieRating;
    private String comment;
    private String username;

    public static MovieRatingViewModel toViewModel(UsersRating usersRating) {
        MovieRatingViewModel movieRatingViewModel = new MovieRatingViewModel();

        movieRatingViewModel.movieRating = usersRating.getUserRating();
        movieRatingViewModel.comment = usersRating.getComment();
        movieRatingViewModel.username = usersRating.getUser().getUsername();

        return movieRatingViewModel;
    }
}
