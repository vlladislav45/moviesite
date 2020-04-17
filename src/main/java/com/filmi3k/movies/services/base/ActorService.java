package com.filmi3k.movies.services.base;

import com.filmi3k.movies.domain.entities.Actor;

public interface ActorService {
    void add(Actor actor);

    Actor findById(int id);

    Actor findByName(String actorName);
}
