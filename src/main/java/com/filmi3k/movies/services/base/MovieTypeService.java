package com.filmi3k.movies.services.base;

import com.filmi3k.movies.domain.entities.MovieType;

import java.util.Set;

public interface MovieTypeService {
    Set<MovieType> findAll();

    MovieType findByMovieType(String genre);
}
