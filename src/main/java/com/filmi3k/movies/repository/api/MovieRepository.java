package com.filmi3k.movies.repository.api;

import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.domain.entities.MovieGenre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    Movie findByMovieName(String movieName);

    @Query(
            value = "SELECT COUNT(*) FROM MOVIE",
            nativeQuery = true)
    long count();

    //    select distinct(movie.movie_name) from movie
    //    inner join movies_genres on movies_genres.movie_id = movie.movie_id
    //    where movies_genres.movie_genre_id IN (1,2);
    Page<Movie> findDistinctByMovieGenresIn(Set<MovieGenre> movieGenres, Pageable pageable);
}
