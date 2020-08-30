package com.jmovies.controllers;

import com.jmovies.domain.entities.Movie;
import com.jmovies.domain.entities.MovieGenre;
import com.jmovies.domain.entities.User;
import com.jmovies.domain.entities.UsersRating;
import com.jmovies.domain.models.binding.RequestAuthorFormBindingModel;
import com.jmovies.domain.models.view.MovieRatingViewModel;
import com.jmovies.domain.models.view.UserRatingViewModel;
import com.jmovies.filters.MovieFilters;
import com.jmovies.filters.MovieSpecification;
import com.jmovies.domain.models.binding.UserRatingBindingModel;
import com.jmovies.domain.models.view.MovieViewModel;
import com.jmovies.repository.api.MovieRepository;
import com.jmovies.services.base.MovieGenreService;
import com.jmovies.services.base.MovieService;
import com.jmovies.services.base.UserService;
import com.jmovies.services.impl.EmailServiceImpl;
import com.jmovies.utils.JSONparser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.SendFailedException;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import static com.jmovies.config.Config.*;
import static com.jmovies.utils.CompressImage.compressImage;

@RestController
public class MovieController {
    private final MovieService movieService;
    private final MovieGenreService movieGenreService;
    private final UserService userService;
    private final EmailServiceImpl emailService;

    @Autowired
    public MovieController(MovieService movieService, MovieGenreService movieGenreService, MovieRepository movieRepository, UserService userService, EmailServiceImpl emailService) {
        this.movieService = movieService;
        this.movieGenreService = movieGenreService;
        this.userService = userService;
        this.emailService = emailService;
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


        String resp = generateResponseFromMovie(filteredMovies.getContent());
        return ResponseEntity.ok()
                .body(resp);
    }

    @GetMapping("/movies/poster/{posterName}")
    public ResponseEntity<?> getPoster(@PathVariable String posterName) throws IOException, URISyntaxException {
        URL url = getClass().getResource(BASE_DIR + "/posters/" + posterName);
        if(url != null) {
            File imagePoster = new File(url.getFile());
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            compressImage(imagePoster, output);

            //ResponseEntity<byte[]>
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf(MediaType.IMAGE_JPEG_VALUE))
                    .body(output.toByteArray());
        }
        return ResponseEntity.ok().body(Map.of("error", posterName + " doesn't exists anymore"));
    }

    @GetMapping("/movies/single/{id}")
    public ResponseEntity<MovieViewModel> getMovieInformation(@PathVariable int id) {
        return ResponseEntity.ok()
                .body(MovieViewModel.toViewModel(movieService.findById(id)));
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
    public ResponseEntity<?> getGenres() {
        // Sort genres by reversed alphabetically and map them by genre names
        List<String> genreNames = movieGenreService.findAll().stream()
                .map(MovieGenre::getMovieGenreName)
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(genreNames);
    }

    @PostMapping("movies/single/rating")
    public ResponseEntity<?> voteSingleMovie(@RequestBody UserRatingBindingModel userRatingBindingModel) {
        /*
          check if the user already exists at the system
          and protection against vote-rigging by the front end
         */
        if (userService.getById(userRatingBindingModel.getUserId()) != null && movieService.findById(userRatingBindingModel.getMovieId()) != null
                && userRatingBindingModel.getMovieRating() >= MIN_VOTE && userRatingBindingModel.getMovieRating() <= MAX_VOTE) {
            User user = userService.getById(userRatingBindingModel.getUserId());
            Movie movie = movieService.findById(userRatingBindingModel.getMovieId());

            if (userRatingBindingModel.getComment() == null) userRatingBindingModel.setComment("");

            if (userService.checkRating(user, movie) == null) // check if the user has not yet voted for the movie
                userService.addUserRating(user, movie, userRatingBindingModel.getMovieRating(), userRatingBindingModel.getComment());
            else {
                return ResponseEntity.ok().body(Map.of("error", "User has already rated"));
            }
        } else {
            return ResponseEntity.ok().body(Map.of("error", "Could not rate movie"));
        }

        // average rating of single movie
        double average = movieService.updateRating(userRatingBindingModel);
        return ResponseEntity.ok()
                .body(Map.of("newRating", average));
    }

    @GetMapping("/movies/single/reviewsByMovie/count")
    public ResponseEntity<?> countReviewsByMovie(@RequestParam int movieId, @RequestParam int page, @RequestParam int size) {
        Movie movie = movieService.findById(movieId);
        Page<UsersRating> ratingPage = movieService.findAllReviewsByMovie(movie, page, size);

        //If has a rating
        if(ratingPage.getContent().size() > 0) {
            return ResponseEntity.ok(Map.of("ratingCount", ratingPage.getTotalElements()));
        }
        return ResponseEntity.ok(Map.of("error", "No such ratings"));
    }

    @GetMapping("/movies/single/reviewsByMovie")
    public ResponseEntity<?> getReviewsByMovie(@RequestParam int movieId, @RequestParam int page, @RequestParam int size) {
        Movie movie = movieService.findById(movieId);
        Page<UsersRating> ratingPage = movieService.findAllReviewsByMovie(movie, page, size);
        List<MovieRatingViewModel> movieRatingViewModels = ratingPage.getContent().stream().map(MovieRatingViewModel::toViewModel).collect(Collectors.toList());

        String resp = "[";
        String ratings = movieRatingViewModels.stream().map(JSONparser::toJson).collect(Collectors.joining(","));
        resp += ratings + "]";

        return ResponseEntity.ok(resp);
    }

    @GetMapping("/movies/single/reviewByAuthorAndMovie")
    public ResponseEntity<?> getReviewByAuthorAndMovie(@RequestParam int userId, @RequestParam int movieId) {
        User user = userService.getById(userId);
        Movie movie = movieService.findById(movieId);
        UsersRating usersRating = userService.checkRating(user,movie);

        if(usersRating != null)
            return ResponseEntity.ok(Map.of("success", UserRatingViewModel.toViewModel(usersRating)));
        return ResponseEntity.ok(Map.of("error", "Doesn't exist rating for this author and movie"));
    }

    @GetMapping("/movies/similar")
    public ResponseEntity<?> getSimilarMovies(@RequestParam int id, @RequestParam int page, @RequestParam int size) {
        Movie movie = movieService.findById(id); //Current movie
        List<String> genreNames = movie.getMovieGenres().stream().map(MovieGenre::getMovieGenreName).collect(Collectors.toList());
        String genre = "";
        if (!genreNames.isEmpty())
            genre = genreNames.get(0);
        //Similar movies by genres
        Page<Movie> pageWithSimilarMovies = movieService.findAll(Specification.where(
                MovieSpecification.withGenres(List.of(genre))
        ), PageRequest.of(page, size));

        /*
        Delete the current movie from similar movies
        Without new ArrayList the object(current movie)
        is not removed
         */
        List<Movie> similarMovies = new ArrayList<>(pageWithSimilarMovies.getContent());
        similarMovies.remove(movie);

        String resp = generateResponseFromMovie(similarMovies);
        return ResponseEntity.ok()
                .body(resp);
    }

    private String generateResponseFromMovie(List<Movie> movies) {
        List<MovieViewModel> moviesViewModels = movies.stream().map(MovieViewModel::toViewModel).collect(Collectors.toList());
        String resp = "[";
        String moviesJson = moviesViewModels.stream().map(JSONparser::toJson).collect(Collectors.joining(","));
        resp += moviesJson + "]";
        return resp;
    }

    /***
     * This method will be used for movie author problems
     * or just for contact
     * TODO: Send the user a letter that the message has been successfully sent
     */
    @PostMapping("/movies/mail")
    public ResponseEntity<?> requestAuthorForm(@RequestBody RequestAuthorFormBindingModel requestAuthorFormModel) {
        if(emailService.requires(requestAuthorFormModel).size() == 0) {
            try {
                emailService.sendSimpleMessage(requestAuthorFormModel);
            } catch (SendFailedException sendFailedException) {
                return ResponseEntity.ok(Map.of("error", "Mail sent failed"));
            }
            return ResponseEntity.ok(Map.of("success", "Mail sent successfully"));
        }
        String resp = "[";
        String errors = emailService.requires(requestAuthorFormModel).stream().map(JSONparser::toJson).collect(Collectors.joining(","));
        resp += errors + "]";
        return ResponseEntity.ok(resp);
    }
}