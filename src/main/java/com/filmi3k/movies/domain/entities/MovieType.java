package com.filmi3k.movies.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "movietype")
@Getter
@Setter
@NoArgsConstructor
public class MovieType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movietype_id", nullable = false, unique = true, updatable = false)
    private int movieTypeId;

    @Column(name = "movietype_label", length = 100, nullable = false, unique = true)
    private String movieTypeLabel;

    public MovieType(String movieTypeLabel) {
        this.movieTypeLabel = movieTypeLabel;
    }

}
