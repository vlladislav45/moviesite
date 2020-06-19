package com.filmi3k.movies.repositories;

import com.filmi3k.movies.MoviesApplication;
import com.filmi3k.movies.domain.entities.Actor;
import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.domain.entities.Poster;
import com.filmi3k.movies.domain.entities.MovieGenre;
import com.filmi3k.movies.repository.api.ActorRepository;
import com.filmi3k.movies.repository.api.DirectorRepository;
import com.filmi3k.movies.repository.api.MovieRepository;
import com.filmi3k.movies.repository.api.MovieGenreRepository;
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
    void testMovieTypeRepo() {
        MovieGenre actual = movieGenreRepository.saveAndFlush(new MovieGenre("Anime"));

        MovieGenre expected = movieGenreRepository.findByMovieGenreName("Anime");
        Assert.assertEquals("Anime movie type is not matched", expected.getMovieGenreName(), actual.getMovieGenreName());
    }

    @Test
    @Transactional
    void testMovieRepo() {
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
    void giveGenres() {
        MovieGenre comedy = movieGenreRepository.findByMovieGenreName("Comedy");
        MovieGenre adventure = movieGenreRepository.findByMovieGenreName("Adventure");
        Set<MovieGenre> genres = new HashSet<>();
        genres.add(comedy);
        genres.add(adventure);

        Movie movie = movieRepository.findByMovieName("Jumanji");
        movie.setMovieGenres(genres);

        movieRepository.saveAndFlush(movie);

        movieRepository.delete(movie);

//        Movie actual = movieRepository.findByMovieName("Onward");
//        Assert.assertEquals("Movie is not exist", movie, actual);
    }

    @Test
    void deleteMovie() {


//        Movie actual = movieRepository.findByMovieName("Onward");
//        Assert.assertEquals("Movie is deleted", movie, actual);
    }

}
