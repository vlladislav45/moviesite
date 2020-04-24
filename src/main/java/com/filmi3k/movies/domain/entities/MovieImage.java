package com.filmi3k.movies.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "movie_images")
@NoArgsConstructor
public class MovieImage {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name = "movie_image_name", nullable = false)
    private String movieImageName;

    @OneToOne(targetEntity = Movie.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "movie_id", nullable = false, unique = true)
    private Movie movie;

    public MovieImage(String movieImageName, Movie movie) {
        this.movieImageName = movieImageName;
        this.movie = movie;
    }

    public String getMovieImageName() {
        return movieImageName;
    }

    public void setMovieImageName(String movieImageName) {
        this.movieImageName = movieImageName;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
