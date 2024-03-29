package com.jmovies.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

/**
   * Join table (users_rating)
 */
@Entity
@Table(name = "users_rating")
@NoArgsConstructor
@Setter
@Getter
public class UsersRating extends BaseEntity{
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    @JsonIgnore
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonIgnore
    @ManyToOne(targetEntity = Movie.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(nullable = false)
    private double userRating;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private Timestamp createdTime;

    public UsersRating(User user, Movie movie, double userRating, String comment) {
        this.user = user;
        this.movie = movie;
        this.userRating = userRating;
        this.comment = comment;
        this.createdTime = Timestamp.valueOf(getDateTimeCreated());
    }
}
