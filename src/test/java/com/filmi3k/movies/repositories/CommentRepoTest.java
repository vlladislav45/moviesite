package com.filmi3k.movies.repositories;

import com.filmi3k.movies.MoviesApplication;
import com.filmi3k.movies.repository.api.CommentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MoviesApplication.class)
public class CommentRepoTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void deleteComment() {
        commentRepository.deleteById(3);
    }
}

