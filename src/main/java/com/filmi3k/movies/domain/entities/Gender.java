package com.filmi3k.movies.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "gender")
@NoArgsConstructor
@Data
public class Gender {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gender_id", nullable = false, updatable = false, unique = true)
    private int genderId;

    @Column(name = "gender_name",length = 15, nullable = false, unique = true)
    private String genderName;

    public Gender(String genderName) { this.genderName = genderName; }
}
