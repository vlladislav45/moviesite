package com.filmi3k.movies.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "login", length = 13, nullable = false, unique = true)
    private String login;

    @Column(name = "pass",  length = 15, nullable = false)
    private String pass;

    @Column(name = "created_time", nullable = false)
    private Date createdTime;

    @ManyToOne(targetEntity = Gender.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "genders", nullable = false)
    private Gender gender;

    @ManyToOne(targetEntity = AccessLevel.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "access_levels", nullable = false)
    private AccessLevel accessLevel;

    //MappedBy Variable user by type User in UserImage class
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserImage> userImages = new ArrayList<>();

    //MappedBy Variable userReview by type User in Review class
    @OneToMany(mappedBy = "userReview", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
}
