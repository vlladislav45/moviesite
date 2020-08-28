package com.jmovies.repository.api;

import com.jmovies.domain.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<UserRole,Integer> {
    UserRole getUserRoleByAuthority(String authority);
}
