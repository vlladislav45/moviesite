package com.filmi3k.movies.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id", nullable = false, unique = true, updatable = false)
    private int movieId;

    @Column(name = "movie_name", nullable = false, unique = true)
    private String movieName;

    @JsonIgnore
    @ManyToMany(targetEntity = MovieType.class,cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "movies_types",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_type_id")
    )
    private Set<MovieType> movieTypes;

    @Column(name = "movie_year", length=4, nullable = false)
    private String movieYear;

    @ManyToOne(targetEntity = Director.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_director_id", nullable = false)
    private Director movieDirector;

    @Column(name = "movie_running_time", nullable = false)
    private int movieRunningTime;

    @ManyToMany(targetEntity = Actor.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "movies_actors",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private Set<Actor> actors;

    @JsonIgnore
    @OneToOne(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private MovieImage movieImage;

    public Movie() {
        movieTypes = new HashSet<>();
        actors = new HashSet<>();
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Set<MovieType> getMovieTypes() {
        return movieTypes;
    }

    public void setMovieTypes(Set<MovieType> movieTypes) {
        this.movieTypes = movieTypes;
    }

    public String getMovieYear() {
        return movieYear;
    }

    public void setMovieYear(String movieYear) {
        this.movieYear = movieYear;
    }

    public Director getMovieDirector() {
        return movieDirector;
    }

    public void setMovieDirector(Director movieDirector) {
        this.movieDirector = movieDirector;
    }

    public int getMovieRunningTime() {
        return movieRunningTime;
    }

    public void setMovieRunningTime(int movieRunningTime) {
        this.movieRunningTime = movieRunningTime;
    }

    public Set<Actor> getActors() {
        return actors;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }

    public MovieImage getMovieImage() {
        return movieImage;
    }

    public void setMovieImage(MovieImage movieImage) {
        this.movieImage = movieImage;
    }
}
