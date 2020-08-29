package com.jmovies.repository.api;

import com.jmovies.domain.entities.Bookmark;
import com.jmovies.domain.entities.Movie;
import com.jmovies.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
    Bookmark getBookmarkByUserAndMovie(User user, Movie movie);
}
