package com.filmi3k.movies.services.base;

import com.filmi3k.movies.domain.entities.Movie;

import java.util.Set;

public interface MovieService {

    void add(Movie movie);

    Movie findById(int id);

    Set<Movie> findAll();

    Movie findByName(String movieName);

    void update(Movie movie);

    void delete(Movie movie);
}
