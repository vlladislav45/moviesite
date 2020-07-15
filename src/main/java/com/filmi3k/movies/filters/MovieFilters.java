package com.filmi3k.movies.filters;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class MovieFilters {
    private List<String> genres = new ArrayList<>();
    private String search = "";
    private int year = 0;
}
