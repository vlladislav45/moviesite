package com.filmi3k.movies.repositories;

import com.filmi3k.movies.MoviesApplication;
import com.filmi3k.movies.domain.entities.Actor;
import com.filmi3k.movies.repository.api.ActorRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest(classes = MoviesApplication.class)
public class ActorRepoTest {
    @Autowired
    private ActorRepository actorRepository;

    @Test
    @Transactional
    void addActor() {
        Actor actor = new Actor("Tom Bari");
        actorRepository.save(actor);

        Assert.assertNotNull(actorRepository.findActorByActorName(actor.getActorName()));
    }
}
