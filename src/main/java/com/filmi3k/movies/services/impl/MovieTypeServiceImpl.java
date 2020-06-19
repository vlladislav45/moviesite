package com.filmi3k.movies.services.impl;

import com.filmi3k.movies.domain.entities.MovieType;
import com.filmi3k.movies.repository.api.MovieTypeRepository;
import com.filmi3k.movies.services.base.MovieTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class MovieTypeServiceImpl implements MovieTypeService {
    private final MovieTypeRepository movieTypeRepository;

    @Autowired
    public MovieTypeServiceImpl(MovieTypeRepository movieTypeRepository) {
        this.movieTypeRepository = movieTypeRepository;
    }

    @Override
    public Set<MovieType> findAll() {
        return (HashSet<MovieType>) movieTypeRepository.findAll();
    }

    @Override
    public MovieType findByMovieType(String genre) {
        MovieType movieType = this.movieTypeRepository.findByMovieTypeLabel(genre);
        return movieType;
    }
}
