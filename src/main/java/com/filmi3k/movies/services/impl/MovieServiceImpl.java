package com.filmi3k.movies.services.impl;

import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.models.binding.UserRatingBindingModel;
import com.filmi3k.movies.repository.api.MovieRepository;
import com.filmi3k.movies.repository.api.UsersRatingRepository;
import com.filmi3k.movies.services.base.MovieService;
import com.filmi3k.movies.utils.Math;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final UsersRatingRepository usersRatingRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, UsersRatingRepository usersRatingRepository) {
        this.movieRepository = movieRepository;
        this.usersRatingRepository = usersRatingRepository;
    }

    @Override
    public void add(Movie movie) {
        this.movieRepository.saveAndFlush(movie);
    }

    @Override
    public Movie findById(int id) {
        Optional<Movie> movie = movieRepository.findById(id);
        return movie.orElse(null);
    }

    @Override
    public Page<Movie> findAll(Specification<Movie> specification, Pageable pageable) {
        return movieRepository.findAll(specification,pageable);
    }

    @Override
    public List<Movie> findAll(Specification<Movie> specification) {
        return movieRepository.findAll(specification);
    }

    @Override
    public long count(Specification<Movie> specification) {
        return movieRepository.count(specification);
    }

    @Override
    public double updateRating(UserRatingBindingModel userRatingBindingModel) {
        double sum = usersRatingRepository.sumAllUsersRatingByMovie(userRatingBindingModel.getMovieId());
        int count = usersRatingRepository.countUsersRatingByMovieId(userRatingBindingModel.getMovieId());

        double average = 0.0;
        if(count != 0)
            average = sum / count;

        Movie movie = this.findById(userRatingBindingModel.getMovieId());
        movie.setMovieRating(Math.round(average,2));
        movieRepository.saveAndFlush(movie);

        return movie.getMovieRating();
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
