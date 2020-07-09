package com.filmi3k.movies.controllers;

import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.domain.entities.MovieGenre;
import com.filmi3k.movies.filters.MovieFilters;
import com.filmi3k.movies.filters.MovieSpecification;
import com.filmi3k.movies.models.view.MoviePosterViewModel;
import com.filmi3k.movies.models.view.MovieViewModel;
import com.filmi3k.movies.repository.api.MovieRepository;
import com.filmi3k.movies.services.base.MovieGenreService;
import com.filmi3k.movies.services.base.MovieService;
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

import static com.filmi3k.movies.config.Config.BASE_DIR;
import static com.filmi3k.movies.utils.CompressImage.compressImage;

@RestController
public class MovieController {
    private final MovieService movieService;
    private final MovieGenreService movieGenreService;

    @Autowired
    public MovieController(MovieService movieService, MovieGenreService movieGenreService, MovieRepository movieRepository) {
        this.movieService = movieService;
        this.movieGenreService = movieGenreService;
    }

    @PostMapping("/movies/count")
    public ResponseEntity<Long> countFilteredMovies(@RequestBody MovieFilters movieFilters) {
        long count = movieService.count(Specification.where(MovieSpecification.withNameLike(movieFilters.getSearch())
                .and(MovieSpecification.withGenres(movieFilters.getGenres()))));

        return ResponseEntity.ok()
                .body(count);
    }

    @PostMapping("/movies")
    public ResponseEntity<String> filteredMovies(@RequestBody MovieFilters movieFilters, @RequestParam int page, @RequestParam int size) {
        Page<Movie> filteredMovies = movieService.findAll(Specification
                        .where(MovieSpecification.withNameLike(movieFilters.getSearch())
                                .and(MovieSpecification.withGenres(movieFilters.getGenres()))),
                PageRequest.of(page, size));

        List<MoviePosterViewModel> moviesViewModels = filteredMovies.stream().map(MoviePosterViewModel::toViewModel).collect(Collectors.toList());
        String resp = "[";
        String moviesJson = moviesViewModels.stream().map(JSONparser::toJson).collect(Collectors.joining(","));
        resp += moviesJson + "]";

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
    public ResponseEntity<Set<MovieGenre>> getGenres() {
        return ResponseEntity.ok()
                .body(movieGenreService.findAll());
    }

}