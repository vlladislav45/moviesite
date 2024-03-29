package com.jmovies.services.impl;

import com.jmovies.domain.entities.Actor;
import com.jmovies.repository.api.ActorRepository;
import com.jmovies.services.base.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ActorServiceImpl implements ActorService {
    private final ActorRepository actorRepository;

    @Autowired
    public ActorServiceImpl(ActorRepository actorRepository) { this.actorRepository = actorRepository; }

    @Override
    public void add(Actor actor) {
        Actor searchedAct = findByName(actor.getActorName());
        if(!searchedAct.getActorName().isEmpty() || searchedAct.getActorName() != null) {
            actorRepository.saveAndFlush(actor);
        }
    }

    @Override
    public Actor findById(int id) {
        Optional<Actor> actor = actorRepository.findById(id);
        return actor.get();
    }

    @Override
    public Actor findByName(String actorName) {
        return actorRepository.findActorByActorName(actorName);
    }
}
