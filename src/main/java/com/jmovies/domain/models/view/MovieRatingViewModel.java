package com.jmovies.domain.models.view;

import com.jmovies.domain.entities.UserImage;
import com.jmovies.domain.entities.UsersRating;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class MovieRatingViewModel {
    private double userRating;
    private String comment;
    private String username;
    private String profileImage = null;
    private Long createdTime;

    public static MovieRatingViewModel toViewModel(UsersRating usersRating) {
        MovieRatingViewModel movieRatingViewModel = new MovieRatingViewModel();

        movieRatingViewModel.userRating = usersRating.getUserRating();
        movieRatingViewModel.comment = usersRating.getComment();
        movieRatingViewModel.username = usersRating.getUser().getUsername();

        //Check if user has an image
        List<UserImage> userImages = generateUserImages(usersRating);
        if(userImages.size() > 0) {
            UserImage userImage = userImages.get(
                    userImages.size() - 1);
            movieRatingViewModel.profileImage = userImage.getImageName();
        }

        movieRatingViewModel.createdTime = usersRating.getCreatedTime().getTime();

        return movieRatingViewModel;
    }

    private static List<UserImage> generateUserImages(UsersRating usersRating) {
        return usersRating.getUser().getUserInfo().getUserImages();
    }
}
