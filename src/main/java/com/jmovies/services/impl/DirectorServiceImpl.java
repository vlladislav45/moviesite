package com.jmovies.services.impl;

import com.jmovies.domain.entities.Director;
import com.jmovies.repository.api.DirectorRepository;
import com.jmovies.services.base.DirectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DirectorServiceImpl implements DirectorService {
    private final DirectorRepository directorRepository;

    @Autowired
    public DirectorServiceImpl(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    @Override
    public void add(Director director) {
        Director searchedDirector = findByName(director.getDirectorName());
        if(!searchedDirector.getDirectorName().isEmpty() || searchedDirector.getDirectorName() != null) {
            directorRepository.saveAndFlush(director);
        }
    }

    @Override
    public Director findById(int id) {
        Optional<Director> director = directorRepository.findById(id);
        return director.get();
    }

    @Override
    public Director findByName(String directorName) {
        return directorRepository.findByDirectorName(directorName);
    }
}
