package com.filmi3k.movies.models.view;

import com.filmi3k.movies.domain.entities.Movie;

public class MoviePosterViewModel {
    private int id;
    private String movieName;
    private int year;
    private String posterName;
    private int movieViews;
    private double movieRating;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public int getMovieViews() {
        return movieViews;
    }

    public void setMovieViews(int movieViews) {
        this.movieViews = movieViews;
    }

    public double getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(double movieRating) {
        this.movieRating = movieRating;
    }

    public static MoviePosterViewModel toViewModel(Movie movie) {
        //TODO: Make small SQL query for these properties
        MoviePosterViewModel viewModel = new MoviePosterViewModel();
        viewModel.id = movie.getMovieId();
        viewModel.movieName = movie.getMovieName();
        viewModel.year = movie.getMovieYear();
        viewModel.posterName = movie.getPoster().getPosterName();
        viewModel.movieViews = movie.getMovieViews();
        viewModel.movieRating = movie.getMovieRating();

        return viewModel;
    }
}
