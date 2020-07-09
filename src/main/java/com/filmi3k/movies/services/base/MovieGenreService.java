package com.filmi3k.movies.services.base;

import com.filmi3k.movies.domain.entities.MovieGenre;

import java.util.Set;

public interface MovieGenreService {
    Set<MovieGenre> findAll();

    MovieGenre findById(int id);

    MovieGenre findByMovieGenreName(String genre);
}
