package com.filmi3k.movies.models.view;

import com.filmi3k.movies.domain.entities.Actor;
import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.domain.entities.MovieGenre;

import java.util.List;
import java.util.stream.Collectors;

public class MovieViewModel {
    private int movieId;
    private String movieName;
    private int movieYear;
    private int movieViews;
    private double movieRating;
    private String movieSummary;
    private String posterName;
    private String directorName;
    List<String> actorNames;
    List<String> movieGenres;

    public MovieViewModel() { }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public int getMovieYear() {
        return movieYear;
    }

    public void setMovieYear(int movieYear) {
        this.movieYear = movieYear;
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

    public String getMovieSummary() {
        return movieSummary;
    }

    public void setMovieSummary(String movieSummary) {
        this.movieSummary = movieSummary;
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public List<String> getActorNames() {
        return actorNames;
    }

    public void setActorNames(List<String> actorNames) {
        this.actorNames = actorNames;
    }

    public List<String> getMovieGenres() {
        return movieGenres;
    }

    public void setMovieGenres(List<String> movieGenres) {
        this.movieGenres = movieGenres;
    }

    public static MovieViewModel toViewModel(Movie movie) {
        MovieViewModel viewModel = new MovieViewModel();
        viewModel.movieId = movie.getMovieId();
        viewModel.movieName = movie.getMovieName();
        viewModel.movieYear = movie.getMovieYear();
        viewModel.movieRating = movie.getMovieRating();
        viewModel.movieViews = movie.getMovieViews();
        viewModel.movieSummary = movie.getMovieSummary();
        viewModel.posterName = movie.getPoster().getPosterName();
        viewModel.directorName = movie.getMovieDirector().getDirectorName();
        viewModel.actorNames = movie.getActors().stream().map(Actor::getActorName).collect(Collectors.toList());
        viewModel.movieGenres = movie.getMovieGenres().stream().map(MovieGenre::getMovieGenreName).collect(Collectors.toList());

        return viewModel;
    }
}
