package com.filmi3k.movies.services.impl;

import com.filmi3k.movies.domain.entities.Review;
import com.filmi3k.movies.repository.api.ReviewRepository;
import com.filmi3k.movies.services.base.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository){
        this.reviewRepository = reviewRepository;
    }

    @Override
    public void add(Review review) {
        reviewRepository.saveAndFlush(review);
    }

    @Override
    public Review getById(int id) {
        Optional<Review> review = reviewRepository.findById(id);
        return review.get();
    }

    @Override
    public void delete(Review review) {
        reviewRepository.delete(review);
    }
}
