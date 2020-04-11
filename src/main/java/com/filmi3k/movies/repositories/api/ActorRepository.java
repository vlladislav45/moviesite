package com.filmi3k.movies.repositories.api;

import com.filmi3k.movies.domain.entities.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {
    Actor findActorByActorName(String actor);
}
