package com.filmi3k.movies.repository.api;

import com.filmi3k.movies.domain.entities.Bookmark;
import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
    Bookmark getBookmarkByUserAndMovie(User user, Movie movie);
}
