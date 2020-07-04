package com.filmi3k.movies.filters;

import java.util.ArrayList;
import java.util.List;

public class MoviesFilter {
    private List<String> genres = new ArrayList<>();

    MoviesFilter() { }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
}
