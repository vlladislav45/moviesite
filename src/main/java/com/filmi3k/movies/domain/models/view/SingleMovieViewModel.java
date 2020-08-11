package com.filmi3k.movies.domain.models.view;

import com.filmi3k.movies.domain.entities.Actor;
import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.domain.entities.MovieGenre;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO: Maybe we dont need this class
 */
@NoArgsConstructor
@Data
public class SingleMovieViewModel {
    private int movieId;
    private String movieName;
    private int movieYear;
    private int movieViews;
    private double movieRating;
    private String movieSummary;
    private String posterName;
    private String directorName;
    private Long movieDuration;
    List<String> actorNames;
    List<String> movieGenres;

    public static SingleMovieViewModel toViewModel(Movie movie) {
        SingleMovieViewModel viewModel = new SingleMovieViewModel();
        viewModel.movieId = movie.getMovieId();
        viewModel.movieName = movie.getMovieName();
        viewModel.movieYear = movie.getMovieYear();
        viewModel.movieRating = movie.getMovieRating();
        viewModel.movieViews = movie.getMovieViews();
        viewModel.movieSummary = movie.getMovieSummary();
        viewModel.posterName = movie.getPoster().getPosterName();
        viewModel.directorName = movie.getMovieDirector().getDirectorName();
        viewModel.movieDuration = movie.getMovieDuration();
        viewModel.actorNames = movie.getActors().stream().map(Actor::getActorName).collect(Collectors.toList());
        viewModel.movieGenres = movie.getMovieGenres().stream().map(MovieGenre::getMovieGenreName).collect(Collectors.toList());

        return viewModel;
    }
}
