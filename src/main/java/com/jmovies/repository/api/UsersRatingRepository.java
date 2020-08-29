package com.jmovies.repository.api;

import com.jmovies.domain.entities.Movie;
import com.jmovies.domain.entities.User;
import com.jmovies.domain.entities.UsersRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<UsersRating> findAllByMovie(Movie movie, Pageable pageable);

    Page<UsersRating> findAllByUser(User user, Pageable pageable);
}
