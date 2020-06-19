package com.filmi3k.movies.repositories;

import com.filmi3k.movies.MoviesApplication;
import com.filmi3k.movies.domain.entities.Comment;
import com.filmi3k.movies.repository.api.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MoviesApplication.class)
public class CommentRepoTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void deleteComment() {
        reviewRepository.deleteById(3);
    }
}

