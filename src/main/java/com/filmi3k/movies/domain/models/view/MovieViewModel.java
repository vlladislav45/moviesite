package com.filmi3k.movies.domain.models.view;

import com.filmi3k.movies.domain.entities.Actor;
import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.domain.entities.MovieGenre;

public class MovieViewModel {
    private int id;
    private String movieName;
    private int year;
    private String posterName;
    private int movieViews;
    private double movieRating;
    private String[] actors;
    private String[] genres;
    private String summary;
    private long duration;

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

    public String[] getActors() {
        return actors;
    }

    public void setActors(String[] actors) {
        this.actors = actors;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public static MovieViewModel toViewModel(Movie movie) {
        //TODO: Make small SQL query for these properties
        MovieViewModel viewModel = new MovieViewModel();
        viewModel.id = movie.getMovieId();
        viewModel.movieName = movie.getMovieName();
        viewModel.year = movie.getMovieYear();
        viewModel.posterName = movie.getPoster().getPosterName();
        viewModel.movieViews = movie.getMovieViews();
        viewModel.movieRating = movie.getMovieRating();
        viewModel.actors = movie.getActors().stream().map(Actor::getActorName).toArray(String[]::new);
        viewModel.genres = movie.getMovieGenres().stream().map(MovieGenre::getMovieGenreName).toArray(String[]::new);
        viewModel.summary = movie.getMovieSummary();
        viewModel.duration = movie.getMovieDuration();

        return viewModel;
    }
}
