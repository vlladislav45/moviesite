package com.jmovies.repository.api;

import com.jmovies.domain.entities.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Integer> {
    Director findByDirectorName(String directorName);
}
