package com.filmi3k.movies.repository.api;

import com.filmi3k.movies.domain.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
    PasswordResetToken findByToken(String token);
}
