package com.filmi3k.movies.repository.api;

import com.filmi3k.movies.domain.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    Movie findByMovieName(String movieName);

    @Query(
            value = "SELECT COUNT(*) FROM MOVIE",
            nativeQuery = true)
    long count();


}
