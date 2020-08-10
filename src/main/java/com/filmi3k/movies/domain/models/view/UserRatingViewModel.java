package com.filmi3k.movies.domain.models.view;

import com.filmi3k.movies.domain.entities.UsersRating;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserRatingViewModel {
    private double movieRating;
    private String comment;
    private String movieName;

    public static UserRatingViewModel toViewModel(UsersRating usersRating) {
        UserRatingViewModel userRatingViewModel = new UserRatingViewModel();

        userRatingViewModel.movieRating = usersRating.getUserRating();
        userRatingViewModel.comment = usersRating.getComment();
        userRatingViewModel.movieName = usersRating.getMovie().getMovieName();

        return userRatingViewModel;
    }
}
