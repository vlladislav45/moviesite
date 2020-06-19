package com.filmi3k.movies.services.base;

import com.filmi3k.movies.domain.entities.Comment;

public interface ReviewService {
    void add(Comment comment);

    Comment getById(int id);

    void delete(Comment comment);
}
