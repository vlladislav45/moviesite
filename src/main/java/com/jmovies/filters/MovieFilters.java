package com.jmovies.filters;

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
    private String searchMovie = "";
    private int year = 0;
    private String actor = "";
    private String director = "";
}
