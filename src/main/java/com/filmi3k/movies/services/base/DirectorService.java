package com.filmi3k.movies.services.base;

import com.filmi3k.movies.domain.entities.Director;

public interface DirectorService {
    void add(Director director);

    Director findById(int id);

    Director findByName(String directorName);
}
