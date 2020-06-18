package com.filmi3k.movies.repository.api;

import com.filmi3k.movies.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User getUserByUsername(String username);
}
