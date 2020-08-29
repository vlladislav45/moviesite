package com.jmovies.services.base;

import com.jmovies.domain.entities.Director;

public interface DirectorService {
    void add(Director director);

    Director findById(int id);

    Director findByName(String directorName);
}
