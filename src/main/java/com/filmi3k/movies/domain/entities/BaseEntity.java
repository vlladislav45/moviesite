package com.filmi3k.movies.domain.entities;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;


abstract class BaseEntity
{
    private final LocalDateTime dateTimeCreated = LocalDateTime.now();

    public BaseEntity() { }

    public LocalDateTime getDateTimeCreated() {
        return dateTimeCreated;
    }

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
