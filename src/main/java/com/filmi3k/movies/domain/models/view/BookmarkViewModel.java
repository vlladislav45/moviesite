package com.filmi3k.movies.domain.models.view;

import com.filmi3k.movies.domain.entities.Movie;

public class BookmarkViewModel {

    private String movieName;

    private int movieId;

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public static BookmarkViewModel toViewModel(Movie m) {
        BookmarkViewModel bvm = new BookmarkViewModel();
        bvm.setMovieId(m.getMovieId());
        bvm.setMovieName(m.getMovieName());
        return bvm;
    }
}
