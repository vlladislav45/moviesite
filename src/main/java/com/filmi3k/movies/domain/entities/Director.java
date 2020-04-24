package com.filmi3k.movies.domain.entities;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "directors")
@NoArgsConstructor
public class Director {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name = "director_name", nullable = false, unique = true)
    private String directorName;

    public Director(String directorName) { this.directorName = directorName; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }
}
