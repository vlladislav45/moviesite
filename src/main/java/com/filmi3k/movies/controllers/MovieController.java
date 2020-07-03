package com.filmi3k.movies.controllers;

import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.domain.entities.MovieGenre;
import com.filmi3k.movies.models.view.MoviePosterViewModel;
import com.filmi3k.movies.models.view.MovieViewModel;
import com.filmi3k.movies.services.base.MovieGenreService;
import com.filmi3k.movies.services.base.MovieService;
import com.filmi3k.movies.utils.JSONparser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
public class MovieController extends BaseController {
    private final MovieService movieService;
    private final MovieGenreService movieGenreService;

    @Autowired
    public MovieController(MovieService movieService, MovieGenreService movieGenreService) {
        this.movieService = movieService;
        this.movieGenreService = movieGenreService;
    }

    @GetMapping("/movies")
    @ResponseBody
    public String getMovies(@RequestParam("count") int count, @RequestParam("offset") int offset) {
        List<Movie> movies = movieService.findAllPaginated(count, offset);
        List<MoviePosterViewModel> moviesViewModels = movies.stream().map(MoviePosterViewModel::toViewModel).collect(Collectors.toList());
        String resp = "[";
        String moviesJson = moviesViewModels.stream().map(JSONparser::toJson).collect(Collectors.joining(","));
        resp += moviesJson + "]";

        return resp;
    }

    @GetMapping("/movies/count")
    @ResponseBody
    public long getAllMovies() {
        return movieService.count();
    }

    @GetMapping("/movies/poster/{posterName}")
    public ResponseEntity<byte[]> getPoster(@PathVariable String posterName) throws IOException, URISyntaxException {
        URL url = getClass().getResource(BASE_DIR + "/posters/" + posterName);
        File imagePoster = new File(url.getFile());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        compressImage(imagePoster, output);

        ResponseEntity<byte[]> retVal = ResponseEntity.ok()
                .contentType(MediaType.valueOf(MediaType.IMAGE_JPEG_VALUE))
                .body(output.toByteArray());
        return retVal;
    }

    @GetMapping("/movies/single/{id}")
    public ResponseEntity<MovieViewModel> getMovieInformation(@PathVariable int id) {
        ResponseEntity<MovieViewModel> movie = ResponseEntity.ok()
                .body(MovieViewModel.toViewModel(movieService.findById(id)));
        return movie;
    }

    @GetMapping("movies/single/hdPoster/{posterName}")
    public ResponseEntity<byte[]> getHdPoster(@PathVariable String posterName) throws IOException {
        URL url = getClass().getResource(BASE_DIR + "/posters/" + posterName);
        File imagePoster = new File(url.getFile());
        byte[] fileContent = Files.readAllBytes(imagePoster.toPath());

        ResponseEntity<byte[]> retVal = ResponseEntity.ok()
                .contentType(MediaType.valueOf(MediaType.IMAGE_JPEG_VALUE))
                .body(fileContent);
        return retVal;
    }

    @GetMapping("movies/genres")
    public ResponseEntity<Set<MovieGenre>> getGenres() {
        ResponseEntity<Set<MovieGenre>> genres = ResponseEntity.ok()
                .body(movieGenreService.findAll());
        return genres;
    }
}
