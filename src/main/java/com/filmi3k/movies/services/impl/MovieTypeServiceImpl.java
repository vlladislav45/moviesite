package com.filmi3k.movies.services.impl;

import com.filmi3k.movies.domain.entities.MovieType;
import com.filmi3k.movies.repositories.api.MovieTypeRepository;
import com.filmi3k.movies.services.base.MovieTypeService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

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
}
