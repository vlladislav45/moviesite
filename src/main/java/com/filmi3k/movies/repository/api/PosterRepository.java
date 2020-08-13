package com.filmi3k.movies.repository.api;

import com.filmi3k.movies.domain.entities.Poster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PosterRepository extends JpaRepository<Poster, Integer> {
    Poster findByPosterName(String posterName);
}
