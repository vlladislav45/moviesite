package com.filmi3k.movies.repository.api;

import com.filmi3k.movies.domain.entities.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
}
