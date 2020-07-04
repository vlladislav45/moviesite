package com.filmi3k.movies.filters;

import java.util.List;

public class MoviesFilter {
    private List<Integer> genreIds;

    MoviesFilter() { }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }
}
