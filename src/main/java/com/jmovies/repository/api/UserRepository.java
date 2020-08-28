package com.jmovies.repository.api;

import com.jmovies.domain.entities.PasswordResetToken;
import com.jmovies.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User getUserByUsername(String username);

    User getUserByEmail(String email);

    @Query(value = "SELECT is_enabled FROM user WHERE user_id=?",
           nativeQuery=true)
    boolean isEnabledUser(int id);

    Set<User> getAllByIsEnabled(boolean isEnabled);

    User getUserByPasswordResetToken(PasswordResetToken passwordResetToken);
}
