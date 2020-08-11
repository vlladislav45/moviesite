package com.filmi3k.movies.domain.models.view;

import com.filmi3k.movies.domain.entities.Actor;
import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.domain.entities.MovieGenre;
import lombok.Data;

@Data
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

    public static MovieViewModel toViewModel(Movie movie) {
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
