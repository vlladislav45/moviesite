package com.filmi3k.movies.services.base;

import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.models.binding.UserRatingBindingModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import java.util.Set;
import java.util.List;

public interface MovieService {

    void add(Movie movie);

    Movie findById(int id);

    Page<Movie> findAll(@Nullable Specification<Movie> specification, Pageable pageable);

    List<Movie> findAll(@Nullable Specification<Movie> specification);

    long count(@Nullable Specification<Movie> specification);

    double updateRating(UserRatingBindingModel movieRatingBindingModel);

    Movie findByName(String movieName);

    void update(Movie movie);

    void delete(Movie movie);
}
