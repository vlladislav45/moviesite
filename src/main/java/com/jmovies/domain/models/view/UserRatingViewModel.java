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
public class UserRatingViewModel {
    private double userRating;
    private String comment;
    private String movieName;
    private String profileImage = null;
    private Long createdTime;

    public static UserRatingViewModel toViewModel(UsersRating usersRating) {
        UserRatingViewModel userRatingViewModel = new UserRatingViewModel();

        userRatingViewModel.userRating = usersRating.getUserRating();
        userRatingViewModel.comment = usersRating.getComment();
        userRatingViewModel.movieName = usersRating.getMovie().getMovieName();

        //Check if user has an image
        List<UserImage> userImages = usersRating.getUser().getUserInfo().getUserImages();
        if(userImages.size() > 0) {
            UserImage userImage = userImages.get(
                    userImages.size() - 1);
            userRatingViewModel.profileImage = userImage.getImageName();
        }

        userRatingViewModel.createdTime = usersRating.getCreatedTime().getTime();

        return userRatingViewModel;
    }
}
