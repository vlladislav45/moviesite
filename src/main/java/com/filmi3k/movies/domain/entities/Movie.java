package com.filmi3k.movies.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "movie")
@Getter
@Setter
@NoArgsConstructor
public class Movie extends BaseEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "movie_id", nullable = false, unique = true, updatable = false)
    private int movieId;

    @Column(name = "movie_name", nullable = false, unique = true)
    private String movieName;

    @ManyToMany(targetEntity = MovieType.class,cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "movies_types",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_type_id")
    )
    private Set<MovieType> movieTypes;

    @Column(name = "movie_year", length=4, nullable = false)
    private String movieYear;

    @ManyToOne(targetEntity = Director.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_director_id", nullable = false)
    private Director movieDirector;

    @Column(name = "movie_running_time", nullable = false)
    private int movieRunningTime;

    @ManyToMany(targetEntity = Actor.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "movies_actors",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private Set<Actor> actors;

    @OneToOne(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private MovieImage moiveImage;

}
