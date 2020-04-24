package com.filmi3k.movies.services.impl;

import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.domain.entities.MovieType;
import com.filmi3k.movies.repositories.api.MovieRepository;
import com.filmi3k.movies.repositories.api.MovieTypeRepository;
import com.filmi3k.movies.services.base.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void add(Movie movie) {
        this.movieRepository.saveAndFlush(movie);
    }

    @Override
    public Movie findById(int id) {
        Optional<Movie> movie = movieRepository.findById(id);
        return movie.get();
    }

    @Override
    public Set<Movie> findAll() {
        Set<Movie> movies = new HashSet<>(movieRepository.findAll());
        return movies;
    }

    @Override
    public Movie findByName(String movieName) {
        return movieRepository.findByMovieName(movieName);
    }

    @Override
    public void update(Movie movie) {
    }

    @Override
    public void delete(Movie movie) { movieRepository.delete(movie); }
}
