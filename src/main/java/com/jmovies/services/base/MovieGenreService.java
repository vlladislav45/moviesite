package com.jmovies.services.base;

import com.jmovies.domain.entities.MovieGenre;

import java.util.Set;

public interface MovieGenreService {
    Set<MovieGenre> findAll();

    MovieGenre findById(int id);

    MovieGenre findByMovieGenreName(String genre);
}
