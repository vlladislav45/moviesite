package com.jmovies.repository.api;

import com.jmovies.domain.entities.User;
import com.jmovies.domain.entities.UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Integer> {
    @Query(value = "SELECT selected_theme FROM user_preferences WHERE user_id=?1",
            nativeQuery=true)
    String getSelectedThemeByUserId(int userId);

    UserPreferences getUserPreferencesByUser(User user);
}
