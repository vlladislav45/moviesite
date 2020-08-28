package com.jmovies.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "anime_episode")
@NoArgsConstructor
@Getter
@Setter
public class AnimeEpisode {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "episode_id", nullable = false, unique = true, updatable = false)
    private int episodeId;

    @Column(name = "episode_title", length = 50, nullable = false)
    private String episodeTitle;

    @Column(name = "episode", nullable = false)
    private int episode;

    @ManyToOne(targetEntity = Anime.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "anime_id", nullable = false)
    private Anime anime;

}
