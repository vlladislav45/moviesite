package com.jmovies.repositories;

import com.jmovies.MoviesApplication;
import com.jmovies.domain.entities.Actor;
import com.jmovies.domain.entities.Movie;
import com.jmovies.domain.entities.Poster;
import com.jmovies.domain.entities.MovieGenre;
import com.jmovies.repository.api.ActorRepository;
import com.jmovies.repository.api.DirectorRepository;
import com.jmovies.repository.api.MovieRepository;
import com.jmovies.repository.api.MovieGenreRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.*;

@SpringBootTest(classes = MoviesApplication.class)
public class MovieRepoTest {
    @Autowired
    private MovieRepository movieRepository; //Instance

    @Autowired
    private MovieGenreRepository movieGenreRepository;

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Test
    @Transactional
    void addMovie() {
        Movie movie = new Movie();
        movie.setMovieName("Test");
        movie.setMovieViews(0);
        movie.setMovieYear(1990);
        movie.setMovieDirector(directorRepository.findByDirectorName("James Wan"));
        movie.setPoster(new Poster("test.jpg",movie));

        MovieGenre horror = movieGenreRepository.findByMovieGenreName("Horror");
        MovieGenre adventure = movieGenreRepository.findByMovieGenreName("Adventure");
        movie.getMovieGenres().add(horror);
        movie.getMovieGenres().add(adventure);

        Actor theRock = actorRepository.findActorByActorName("Al Pacino");
        movie.getActors().add(theRock);

        movieRepository.saveAndFlush(movie);

        Movie actual = movieRepository.findByMovieName("Test");

        Assert.assertEquals("Movie is not exist", movie, actual);

    }

    @Test
    @Transactional
    void getAllMovies() {
        Set<Movie> movies = new HashSet<>(movieRepository.findAll());

        for (Movie movie: movies) {
            System.out.println(movie.getMovieName());
        }
    }

    @Test
    @Transactional
    void takeGenres() {
        MovieGenre comedy = movieGenreRepository.findByMovieGenreName("Comedy");
        MovieGenre adventure = movieGenreRepository.findByMovieGenreName("Adventure");
        Set<MovieGenre> genres = new HashSet<>();
        genres.add(comedy);
        genres.add(adventure);

        Movie movie = movieRepository.findByMovieName("Onward");
        movie.setMovieGenres(genres);

        movieRepository.saveAndFlush(movie);

        Movie actual = movieRepository.findByMovieName("Onward");
        Assert.assertEquals("Movie is not exist", movie, actual);
    }
}
