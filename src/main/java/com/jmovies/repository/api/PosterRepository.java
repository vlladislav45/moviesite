package com.jmovies.repository.api;

import com.jmovies.domain.entities.Poster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PosterRepository extends JpaRepository<Poster, Integer> {
    Poster findByPosterName(String posterName);
}
