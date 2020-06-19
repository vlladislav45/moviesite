package com.filmi3k.movies.domain.entities;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "director")
@NoArgsConstructor
public class Director {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "director_id", nullable = false, unique = true, updatable = false)
    private int directorId;

    @Column(name = "director_name", length = 50, nullable = false, unique = true)
    private String directorName;

    public Director(String directorName) { this.directorName = directorName; }

    public int getId() {
        return directorId;
    }

    public void setId(int id) {
        this.directorId = id;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }
}
