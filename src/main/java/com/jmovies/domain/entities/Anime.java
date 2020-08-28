package com.jmovies.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "anime")
@NoArgsConstructor
@Getter
@Setter
public class Anime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "anime_id", nullable = false, unique = true, updatable = false)
    private int animeId;

    @Column(name = "anime_name", length = 50, nullable = false, unique = true)
    private String animeName;

    @Column(name = "actor_name", length = 50, nullable = false, unique = true)
    private String actorName;

    @Column(name = "anime_rating", nullable = false, unique = true)
    private double animeRating;
}
