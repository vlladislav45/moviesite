package com.filmi3k.movies.domain.entities;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "poster")
@NoArgsConstructor
public class Poster {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "poster_id", nullable = false, unique = true, updatable = false)
    private int posterId;

    @Column(name = "poster_name", nullable = false)
    private String posterName;

    @OneToOne(targetEntity = Movie.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "movie_id", nullable = true, unique = true)
    private Movie movie;

    @OneToOne(targetEntity = Anime.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
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

    public int getPosterId() {
        return posterId;
    }

    public void setPosterId(int posterId) {
        this.posterId = posterId;
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
