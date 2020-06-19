package com.filmi3k.movies.services.impl;

import com.filmi3k.movies.domain.entities.Comment;
import com.filmi3k.movies.repository.api.CommentRepository;
import com.filmi3k.movies.services.base.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    @Override
    public void add(Comment comment) {
        commentRepository.saveAndFlush(comment);
    }

    @Override
    public Comment getById(int id) {
        Optional<Comment> review = commentRepository.findById(id);
        return review.get();
    }

    @Override
    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }
}
