package com.filmi3k.movies.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "user_images")
@Getter
@Setter
@NoArgsConstructor
public class UserImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name = "image_name", nullable = false, unique = true)
    private String imageName;

    @ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "image_username_id", nullable = false)
    private User user;

    @Column(name = "image_date", nullable = false)
    private Date imgDate;
}
