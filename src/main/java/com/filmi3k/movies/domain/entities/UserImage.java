package com.filmi3k.movies.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class UserImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "user_image_id", nullable = false, unique = true, updatable = false)
    private int userImageId;

    @Column(name = "image_name", nullable = false, unique = true)
    private String imageName;

    @JsonIgnore
    @Column(name = "image_created", nullable = false)
    private LocalDateTime imageCreated;

    @JsonIgnore
    @ManyToOne(targetEntity = UserInfo.class)
    @JoinColumn(name = "user_info_id", nullable = false)
    private UserInfo userInfo;

    public UserImage(String imageName, UserInfo userInfo) {
        this.imageCreated = getDateTimeCreated();
        this.imageName = imageName;
        this.userInfo = userInfo;
    }
}
