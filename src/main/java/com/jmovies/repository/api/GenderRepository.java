package com.jmovies.repository.api;

import com.jmovies.domain.entities.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Integer> {
    Gender findByGenderName(String genderName);
}
