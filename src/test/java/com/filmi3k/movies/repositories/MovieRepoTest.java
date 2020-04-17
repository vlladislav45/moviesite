package com.filmi3k.movies.repositories;

import com.filmi3k.movies.MoviesApplication;
import com.filmi3k.movies.domain.entities.Actor;
import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.domain.entities.MovieImage;
import com.filmi3k.movies.domain.entities.MovieType;
import com.filmi3k.movies.repositories.api.ActorRepository;
import com.filmi3k.movies.repositories.api.DirectorRepository;
import com.filmi3k.movies.repositories.api.MovieRepository;
import com.filmi3k.movies.repositories.api.MovieTypeRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.*;

@SpringBootTest(classes = MoviesApplication.class)
public class MovieRepoTest {
    @Autowired
    private MovieRepository movieRepository; //Instance

    @Autowired
    private MovieTypeRepository movieTypeRepository;

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Test
    @Transactional
    void testMovieTypeRepo() {
        MovieType actual = movieTypeRepository.saveAndFlush(new MovieType("Anime"));

        MovieType expected = movieTypeRepository.findByMovieTypeLabel("Anime");
        Assert.assertEquals("Anime movie type is not matched", expected.getMovieTypeLabel(), actual.getMovieTypeLabel());
    }

    @Test
    @Transactional
    void testMovieRepo() {
        Movie movie = new Movie();
        movie.setMovieName("Test");
        movie.setMovieRunningTime(0);
        movie.setMovieYear("1990");
        movie.setMovieDirector(directorRepository.findByDirectorName("James Wan"));
        movie.setMovieImage(new MovieImage("test.jpg", movie));

        MovieType horror = movieTypeRepository.findByMovieTypeLabel("Horror");
        MovieType adventure = movieTypeRepository.findByMovieTypeLabel("Adventure");
        movie.getMovieTypes().add(horror);
        movie.getMovieTypes().add(adventure);

        Actor theRock = actorRepository.findActorByActorName("Al Pacino");
        movie.getActors().add(theRock);

        movieRepository.saveAndFlush(movie);

        Movie actual = movieRepository.findByMovieName("Test");

        Assert.assertEquals("Movie is not exist", movie, actual);

    }
}
