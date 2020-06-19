package com.filmi3k.movies.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
abstract class BaseEntity
{
    private LocalDate dateCreated = LocalDate.now();
}
