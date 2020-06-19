package com.filmi3k.movies.repository.api;

import com.filmi3k.movies.domain.entities.MovieType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieTypeRepository extends JpaRepository<MovieType, Integer> {
    MovieType findByMovieTypeLabel(String moveTypeLabel);
}
