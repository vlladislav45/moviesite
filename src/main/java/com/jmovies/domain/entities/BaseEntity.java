package com.jmovies.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
abstract class BaseEntity
{
    @JsonIgnore
    private final LocalDateTime dateTimeCreated = LocalDateTime.now();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity)) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(dateTimeCreated, that.dateTimeCreated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTimeCreated);
    }
}
