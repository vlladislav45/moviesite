package com.filmi3k.movies.repository.api;

import com.filmi3k.movies.domain.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<UserRole,Integer> {
    UserRole getUserRoleByAuthority(String authority);
}
