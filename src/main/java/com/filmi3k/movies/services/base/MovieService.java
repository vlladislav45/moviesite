package com.filmi3k.movies.services.base;

import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.domain.models.binding.SingleMovieBindingModel;
import com.filmi3k.movies.domain.models.binding.UserRatingBindingModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

public interface MovieService {
    void add(SingleMovieBindingModel singleMovieBindingModel);

    Map<String, String> checkMovieFields(SingleMovieBindingModel singleMovieBindingModel);

    Movie findById(int id);

    Page<Movie> findAll(@Nullable Specification<Movie> specification, Pageable pageable);

    long count(@Nullable Specification<Movie> specification);

    double updateRating(UserRatingBindingModel movieRatingBindingModel);

    Movie findByName(String movieName);

    void delete(Movie movie);
}
