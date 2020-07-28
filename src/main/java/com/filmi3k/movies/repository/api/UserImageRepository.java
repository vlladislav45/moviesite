package com.filmi3k.movies.repository.api;

import com.filmi3k.movies.domain.entities.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserImageRepository extends JpaRepository<UserImage, Integer> {
}
