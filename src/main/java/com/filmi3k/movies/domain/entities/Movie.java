package com.filmi3k.movies.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movie")
@Data
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id", nullable = false, unique = true, updatable = false)
    private int movieId;

    @Column(name = "movie_name", nullable = false)
    private String movieName;

    @ManyToMany(targetEntity = MovieGenre.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "movies_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_genre_id")
    )
    private Set<MovieGenre> movieGenres;

    @Column(name = "movie_year", length=4, nullable = false)
    private int movieYear;

    @ManyToOne(targetEntity = Director.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_director_id", nullable = false)
    private Director movieDirector;

    @Column(name = "movie_views", nullable = false)
    private int movieViews;

    @Column(name = "movie_rating", nullable = false)
    private double movieRating;

    @Column(name = "movie_summary", nullable = false)
    @Type(type = "text")
    private String movieSummary;

    @ManyToMany(targetEntity = Actor.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "movies_actors",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private Set<Actor> actors;

    @JsonIgnore
    @OneToOne(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Poster poster;

    @JsonIgnore
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<UsersRating> usersRatings = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "movie",  cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Bookmark> bookMarks = new HashSet<>();

    @Column(name = "movie_duration")
    private Time movieDuration;

    public Movie() {
        movieGenres = new HashSet<>();
        actors = new HashSet<>();
    }
}
