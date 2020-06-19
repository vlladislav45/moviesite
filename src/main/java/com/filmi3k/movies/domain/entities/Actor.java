package com.filmi3k.movies.domain.entities;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "actor")
@NoArgsConstructor
public class Actor {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "actor_id", nullable = false, unique = true, updatable = false)
    private int actorId;

    @Column(name = "actor_name", length = 50, nullable = false, unique = true)
    private String actorName;

    public Actor(String actorName) { this.actorName = actorName; }

    public int getActorId() {
        return actorId;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }
}
