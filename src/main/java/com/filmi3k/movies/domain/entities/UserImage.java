package com.filmi3k.movies.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_image")
@Getter
@Setter
@NoArgsConstructor
public class UserImage {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "user_image_id", nullable = false, unique = true, updatable = false)
    private int userImageId;

    @Column(name = "image_name", nullable = false, unique = true)
    private String imageName;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "image_user_id", nullable = false)
    private User user;

    @Column(name = "image_created", nullable = false)
    private LocalDateTime imageCreated;
}
