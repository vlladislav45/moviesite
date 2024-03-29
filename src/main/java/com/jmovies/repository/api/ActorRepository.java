package com.jmovies.repository.api;

import com.jmovies.domain.entities.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {
    List<Actor> findByActorNameIn(List<String> actors);

    Actor findActorByActorName(String actor);
}
