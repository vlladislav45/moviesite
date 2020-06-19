package com.filmi3k.movies.services.base;

import com.filmi3k.movies.domain.entities.Movie;

import java.util.Set;
import java.util.List;

public interface MovieService {

    void add(Movie movie);

    Movie findById(int id);

    Set<Movie> findAll();

    List<Movie> findAllPaginated(int count, int offset);

    long count();

    Movie findByName(String movieName);

    void update(Movie movie);

    void delete(Movie movie);
}
