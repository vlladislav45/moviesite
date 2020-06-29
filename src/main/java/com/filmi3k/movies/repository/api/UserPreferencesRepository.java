package com.filmi3k.movies.repository.api;

import com.filmi3k.movies.domain.entities.UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Integer> {
}
