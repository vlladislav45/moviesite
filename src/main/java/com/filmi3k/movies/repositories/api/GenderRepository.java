package com.filmi3k.movies.repositories.api;

import com.filmi3k.movies.domain.entities.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Integer> {

    Gender findByGenderName(String genderName);
}
