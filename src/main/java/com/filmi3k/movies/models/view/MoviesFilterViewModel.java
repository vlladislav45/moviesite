package com.filmi3k.movies.models.view;

import java.util.List;

public class MoviesFilterViewModel {
    private List<Integer> genreIds;

    MoviesFilterViewModel() { }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }
}
