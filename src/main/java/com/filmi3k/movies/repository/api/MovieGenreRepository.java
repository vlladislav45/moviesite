package com.filmi3k.movies.repository.api;

import com.filmi3k.movies.domain.entities.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieGenreRepository extends JpaRepository<MovieGenre, Integer> {
    MovieGenre findByMovieGenreName(String movieGenre);
}
