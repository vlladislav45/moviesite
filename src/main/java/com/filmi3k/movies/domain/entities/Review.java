package com.filmi3k.movies.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    @ManyToOne(targetEntity = Movie.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "review_movie_id", nullable = false)
    private Movie movie;

    @Column(name = "review_date", nullable = false)
    private Date reviewDate;

    @ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "review_user_id", nullable = false)
    private User userReview;

    @Column(name = "review_comment", nullable = false)
    private String reviewComment;

    @Column(name = "review_rating", nullable = false)
    private int reviewRating;

}
