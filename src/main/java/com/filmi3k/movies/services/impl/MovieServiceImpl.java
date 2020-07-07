package com.filmi3k.movies.services.impl;

import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.domain.entities.MovieGenre;
import com.filmi3k.movies.repository.api.MovieRepository;
import com.filmi3k.movies.services.base.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

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
        return new HashSet<>(movieRepository.findAll());
    }

    @Override
    public Page<Movie> findAll(Specification<Movie> specification, Pageable pageable) {
        return movieRepository.findAll(specification,pageable);
    }

    @Override
    public List<Movie> findAllPaginated(int count, int offset) {
        Pageable p = PageRequest.of(offset, count, Sort.by("movieRating").descending());
        return movieRepository.findAll(p).getContent();
    }

    @Override
    public long count(Specification<Movie> specification) {
        return movieRepository.count(specification);
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
