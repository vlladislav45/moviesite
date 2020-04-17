package com.filmi3k.movies.services.base;

import com.filmi3k.movies.domain.entities.Review;

public interface ReviewService {
    void add(Review review);

    Review getById(int id);

    void delete(Review review);
}
