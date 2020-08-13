package com.filmi3k.movies.repository.api;

import com.filmi3k.movies.domain.entities.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieGenreRepository extends JpaRepository<MovieGenre, Integer> {
    List<MovieGenre> findByMovieGenreNameIn(List<String> genres);

    MovieGenre findByMovieGenreName(String movieGenre);
}
