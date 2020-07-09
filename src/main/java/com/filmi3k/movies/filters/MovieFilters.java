package com.filmi3k.movies.filters;

import java.util.ArrayList;
import java.util.List;

public class MovieFilters {
    private List<String> genres = new ArrayList<>();
    private String search = "";

    public MovieFilters() { }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
