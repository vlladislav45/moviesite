package com.filmi3k.movies.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.domain.entities.MovieGenre;
import com.filmi3k.movies.models.view.MoviePosterViewModel;
import com.filmi3k.movies.models.view.MoviesFilterViewModel;
import com.filmi3k.movies.services.base.MovieGenreService;
import com.filmi3k.movies.services.base.MovieService;
import com.filmi3k.movies.utils.JSONparser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class MoviesFilterController {
    private final MovieService movieService;
    private final MovieGenreService movieGenreService;

    @Autowired
    public MoviesFilterController(MovieService movieService, MovieGenreService movieGenreService) {
        this.movieService = movieService;
        this.movieGenreService = movieGenreService;
    }

    @PostMapping("movies/filter")
    public ResponseEntity<String> filteredMoviesByGenre(@RequestBody MoviesFilterViewModel moviesFilterViewModel, @RequestParam int page, @RequestParam int size) {
        Set<MovieGenre> movieGenres = new HashSet<>();
        for(int i = 0; i < moviesFilterViewModel.getGenreIds().size(); i++) {
            movieGenres.add(movieGenreService.findById(moviesFilterViewModel.getGenreIds().get(i)));
        }

        Page<Movie> pageMovies = null;
        for(MovieGenre movieGenre : movieGenres) {
            pageMovies = movieService.browseMoviesByGenre(movieGenre,page, size);
        }
        List<Movie> movies = pageMovies.getContent();
        List<MoviePosterViewModel> moviesViewModels = movies.stream().map(MoviePosterViewModel::toViewModel).collect(Collectors.toList());
        String resp = "[";
        String moviesJson = moviesViewModels.stream().map(JSONparser::toJson).collect(Collectors.joining(","));
        resp += moviesJson + "]";

        return new ResponseEntity<String>(
                resp,
                HttpStatus.OK
        );
    }
}
