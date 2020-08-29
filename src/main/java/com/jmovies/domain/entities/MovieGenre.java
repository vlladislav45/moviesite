package com.jmovies.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movie_genre")
@Getter
@Setter
@NoArgsConstructor
public class MovieGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id", nullable = false, unique = true, updatable = false)
    private int genreId;

    @Column(name = "movie_genre_name", length = 50, nullable = false, unique = true)
    private String movieGenreName;

    public MovieGenre(String movieGenreName) {
        this.movieGenreName = movieGenreName;
    }

}
