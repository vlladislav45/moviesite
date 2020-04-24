package com.filmi3k.movies.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
abstract class BaseEntity
{
//    private int id;

    //date here and date in db are slightly different with same nano seconds,
    //because there is time between the creation of the object and the save
    private LocalDate dateCreated = LocalDate.now();

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof BaseEntity)) return false;
//        BaseEntity that = (BaseEntity) o;
//        return id == that.id;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id);
//    }
}
