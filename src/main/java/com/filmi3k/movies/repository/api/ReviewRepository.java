package com.filmi3k.movies.repository.api;

import com.filmi3k.movies.domain.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Comment, Integer> {
}
