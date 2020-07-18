package com.filmi3k.movies.repository.api;

import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.domain.entities.User;
import com.filmi3k.movies.domain.entities.UsersRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRatingRepository extends JpaRepository<UsersRating, Integer> {
    UsersRating findUsersRatingByUserAndMovie(User user, Movie movie);

    @Query(value = "SELECT sum(user_rating) FROM users_rating WHERE movie_id=?1", nativeQuery=true)
    double sumAllUsersRatingByMovie(int movieId);

    @Query(value = "SELECT COUNT(user_rating) FROM users_rating WHERE movie_id=?1", nativeQuery=true)
    int countUsersRatingByMovieId(int movieId);
}
