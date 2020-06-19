package com.filmi3k.movies.services.base;

import com.filmi3k.movies.domain.entities.Comment;

public interface CommentService {
    void add(Comment comment);

    Comment getById(int id);

    void delete(Comment comment);
}
