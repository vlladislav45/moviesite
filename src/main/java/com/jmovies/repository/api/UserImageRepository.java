package com.jmovies.repository.api;

import com.jmovies.domain.entities.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserImageRepository extends JpaRepository<UserImage, Integer> {
}
