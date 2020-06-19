package com.filmi3k.movies.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false, unique = true, updatable = false)
    private int commentId;

    @Column(name = "comment_created", nullable = false)
    private LocalDateTime commentCreated;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "comment_rating", nullable = false)
    private int commentRating;

    @ManyToOne(targetEntity = Movie.class)
    @JoinColumn(name = "comment_movie_id", nullable = true)
    private Movie movie;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "comment_user_id", nullable = false)
    private User user;

    @ManyToOne(targetEntity = Anime.class)
    @JoinColumn(name = "comment_anime_id", nullable = true)
    private Anime anime;
}
