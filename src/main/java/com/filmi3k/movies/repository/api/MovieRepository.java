package com.filmi3k.movies.repository.api;

import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.domain.entities.MovieGenre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    Movie findByMovieName(String movieName);

    @Query(
            value = "SELECT COUNT(*) FROM MOVIE",
            nativeQuery = true)
    long count();

    Page<Movie> findAllByMovieGenres(MovieGenre movieGenre, Pageable pageable);
}
