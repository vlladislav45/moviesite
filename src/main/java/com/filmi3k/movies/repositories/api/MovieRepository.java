package com.filmi3k.movies.repositories.api;

import com.filmi3k.movies.domain.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    Movie findByMovieName(String movieName);

}
