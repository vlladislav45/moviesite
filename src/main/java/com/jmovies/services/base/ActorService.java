package com.jmovies.services.base;

import com.jmovies.domain.entities.Actor;

public interface ActorService {
    void add(Actor actor);

    Actor findById(int id);

    Actor findByName(String actorName);
}
