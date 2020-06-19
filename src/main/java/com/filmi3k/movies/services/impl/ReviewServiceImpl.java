package com.filmi3k.movies.services.impl;

import com.filmi3k.movies.domain.entities.Comment;
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
    public void add(Comment comment) {
        reviewRepository.saveAndFlush(comment);
    }

    @Override
    public Comment getById(int id) {
        Optional<Comment> review = reviewRepository.findById(id);
        return review.get();
    }

    @Override
    public void delete(Comment comment) {
        reviewRepository.delete(comment);
    }
}
