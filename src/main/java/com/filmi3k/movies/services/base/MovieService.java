package com.filmi3k.movies.services.base;

import com.filmi3k.movies.domain.entities.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import java.util.Set;
import java.util.List;

public interface MovieService {

    void add(Movie movie);

    Movie findById(int id);

    Set<Movie> findAll();

    Page<Movie> findAll(@Nullable Specification<Movie> specification, Pageable pageable);

    List<Movie> findAllPaginated(int count, int offset);

    long count(@Nullable Specification<Movie> specification);

    Movie findByName(String movieName);

    void update(Movie movie);

    void delete(Movie movie);
}
