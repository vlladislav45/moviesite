package com.jmovies.services.impl;

import com.jmovies.domain.entities.MovieGenre;
import com.jmovies.repository.api.MovieGenreRepository;
import com.jmovies.services.base.MovieGenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class MovieGenreServiceImpl implements MovieGenreService {
    private final MovieGenreRepository movieGenreRepository;

    @Autowired
    public MovieGenreServiceImpl(MovieGenreRepository movieGenreRepository) {
        this.movieGenreRepository = movieGenreRepository;
    }

    @Override
    public Set<MovieGenre> findAll() {
        return new HashSet<>(movieGenreRepository.findAll());
    }

    @Override
    public MovieGenre findById(int id) {
        Optional<MovieGenre> movieGenreOptional = movieGenreRepository.findById(id);
        return movieGenreOptional.get();
    }

    @Override
    public MovieGenre findByMovieGenreName(String genre) {
        return this.movieGenreRepository.findByMovieGenreName(genre);
    }
}
