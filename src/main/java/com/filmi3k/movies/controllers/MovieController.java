package com.filmi3k.movies.controllers;

import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.domain.entities.MovieGenre;
import com.filmi3k.movies.domain.entities.User;
import com.filmi3k.movies.domain.models.view.MovieRatingViewModel;
import com.filmi3k.movies.filters.MovieFilters;
import com.filmi3k.movies.filters.MovieSpecification;
import com.filmi3k.movies.domain.models.binding.UserRatingBindingModel;
import com.filmi3k.movies.domain.models.view.MovieViewModel;
import com.filmi3k.movies.domain.models.view.SingleMovieViewModel;
import com.filmi3k.movies.repository.api.MovieRepository;
import com.filmi3k.movies.services.base.MovieGenreService;
import com.filmi3k.movies.services.base.MovieService;
import com.filmi3k.movies.services.base.UserService;
import com.filmi3k.movies.utils.JSONparser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import static com.filmi3k.movies.config.Config.*;
import static com.filmi3k.movies.utils.CompressImage.compressImage;

@RestController
public class MovieController {
    private final MovieService movieService;
    private final MovieGenreService movieGenreService;
    private final UserService userService;

    @Autowired
    public MovieController(MovieService movieService, MovieGenreService movieGenreService, MovieRepository movieRepository, UserService userService) {
        this.movieService = movieService;
        this.movieGenreService = movieGenreService;
        this.userService = userService;
    }

    @PostMapping("/movies/count")
    public ResponseEntity<Map<String, Object>> countFilteredMovies(@RequestBody MovieFilters movieFilters) {
        Map<String, Object> response = new HashMap<>();
        long count = movieService.count(Specification.where(MovieSpecification.countWithGenres(movieFilters.getGenres())
                .and(MovieSpecification.withFilter(movieFilters))));

        response.put("count", count);
        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping("/movies")
    public ResponseEntity<String> filteredMovies(@RequestBody MovieFilters movieFilters, @RequestParam int page, @RequestParam int size) {
        Page<Movie> filteredMovies = movieService.findAll(Specification
                        .where(MovieSpecification.withFilter(movieFilters)
                                .and(MovieSpecification.withGenres(movieFilters.getGenres()))
                        ),
                PageRequest.of(page, size));


        String resp = generateResponseFromMovie(filteredMovies);
        return ResponseEntity.ok()
                .body(resp);
    }

    @GetMapping("/movies/poster/{posterName}")
    public ResponseEntity<byte[]> getPoster(@PathVariable String posterName) throws IOException, URISyntaxException {
        URL url = getClass().getResource(BASE_DIR + "/posters/" + posterName);
        File imagePoster = new File(url.getFile());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        compressImage(imagePoster, output);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(MediaType.IMAGE_JPEG_VALUE))
                .body(output.toByteArray());
    }

    @GetMapping("/movies/single/{id}")
    public ResponseEntity<SingleMovieViewModel> getMovieInformation(@PathVariable int id) {
        return ResponseEntity.ok()
                .body(SingleMovieViewModel.toViewModel(movieService.findById(id)));
    }

    @GetMapping("/movies/single/hdPoster/{posterName}")
    public ResponseEntity<byte[]> getHdPoster(@PathVariable String posterName) throws IOException {
        URL url = getClass().getResource(BASE_DIR + "/posters/" + posterName);
        File imagePoster = new File(url.getFile());
        byte[] fileContent = Files.readAllBytes(imagePoster.toPath());

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(MediaType.IMAGE_JPEG_VALUE))
                .body(fileContent);
    }

    @GetMapping("/movies/genres")
    public ResponseEntity<Set<MovieGenre>> getGenres() {
        return ResponseEntity.ok()
                .body(movieGenreService.findAll());
    }

    @PostMapping("movies/single/rating")
    public ResponseEntity<Map<String, Object>> voteSingleMovie(@RequestBody UserRatingBindingModel userRatingBindingModel) {
        Map<String, Object> response = new HashMap<>();
        /**
         * check if the user already exists at the system
         * and protection against vote-rigging by the front end
         */
        if (userService.getById(userRatingBindingModel.getUserId()) != null && movieService.findById(userRatingBindingModel.getMovieId()) != null
                && userRatingBindingModel.getMovieRating() >= MIN_VOTE && userRatingBindingModel.getMovieRating() <= MAX_VOTE) {
            User user = userService.getById(userRatingBindingModel.getUserId());
            Movie movie = movieService.findById(userRatingBindingModel.getMovieId());

            if (userRatingBindingModel.getComment() == null) userRatingBindingModel.setComment("");

            if (userService.checkRating(user, movie) == null) // check if the user has not yet voted for the movie
                userService.addUserRating(user, movie, userRatingBindingModel.getMovieRating(), userRatingBindingModel.getComment());
            else {
                response.put("error", "User has already rated");
                return ResponseEntity.ok().body(response);
            }
        } else {
            response.put("error", "Could not rate movie");
            return ResponseEntity.ok().body(response);
        }

        // average rating of single movie
        double average = movieService.updateRating(userRatingBindingModel);

        response.put("newRating", average);
        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("/movies/single/reviewsByMovie")
    public ResponseEntity<?> getReviewsByMovie(@RequestParam int movieId) {
        Movie movie = movieService.findById(movieId);
        Set<MovieRatingViewModel> movieRatingViewModels = movie.getUsersRatings().stream().map(MovieRatingViewModel::toViewModel).collect(Collectors.toSet());

        String resp = "[";
        String moviesJson = movieRatingViewModels.stream().map(JSONparser::toJson).collect(Collectors.joining(","));
        resp += moviesJson + "]";

        return ResponseEntity.ok(resp);
    }

    @GetMapping("/movies/similar")
    public ResponseEntity<?> getSimilarMovies(@RequestParam int id, @RequestParam int page, @RequestParam int size) {
        Movie movie = movieService.findById(id);
        List<String> genreNames = movie.getMovieGenres().stream().map(MovieGenre::getMovieGenreName).collect(Collectors.toList());
        String genre = "";
        if (!genreNames.isEmpty())
            genre = genreNames.get(0);
        //Similar movies by genres
        Page<Movie> similarMovies = movieService.findAll(Specification.where(
                MovieSpecification.withGenres(List.of(genre))
        ), PageRequest.of(page, size));

        String resp = generateResponseFromMovie(similarMovies);
        return ResponseEntity.ok()
                .body(resp);
    }


    private String generateResponseFromMovie(Page<Movie> movies) {
        List<MovieViewModel> moviesViewModels = movies.stream().map(MovieViewModel::toViewModel).collect(Collectors.toList());
        String resp = "[";
        String moviesJson = moviesViewModels.stream().map(JSONparser::toJson).collect(Collectors.joining(","));
        resp += moviesJson + "]";
        return resp;
    }
}