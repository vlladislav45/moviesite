package com.filmi3k.movies.domain.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@Entity
@Table(name = "poster")
@NoArgsConstructor
@Data
public class Poster {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "poster_id", nullable = false, unique = true, updatable = false)
    private int posterId;

    @Column(name = "poster_name", nullable = false)
    private String posterName;

    @OneToOne(targetEntity = Movie.class)
    @JoinColumn(name = "movie_id", nullable = true, unique = true)
    private Movie movie;

    @OneToOne(targetEntity = Anime.class)
    @JoinColumn(name = "anime_id", nullable = true, unique = true)
    private Anime anime;

    public Poster(String posterName, Movie movie) {
        this.posterName = posterName;
        this.movie = movie;
    }

    public Poster(String posterName, Anime anime) {
        this.posterName = posterName;
        this.anime = anime;
    }
}
