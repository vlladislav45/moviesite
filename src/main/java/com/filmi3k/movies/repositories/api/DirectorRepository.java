package com.filmi3k.movies.repositories.api;

import com.filmi3k.movies.domain.entities.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Integer> {
    Director findByDirectorName(String directorName);
}
