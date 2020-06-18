package com.filmi3k.movies.controllers;

import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.models.view.MovieViewModel;
import com.filmi3k.movies.services.base.MovieService;
import com.filmi3k.movies.services.base.UserService;
import com.filmi3k.movies.utils.JSONparser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MovieController extends BaseController {
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService, UserService userService) {
        this.movieService = movieService;
    }

    @GetMapping("/movies")
    @ResponseBody
    public String getMovies(@RequestParam("count") int count, @RequestParam("offset") int offset) {
        List<Movie> movies = movieService.findAllPaginated(count, offset);
        List<MovieViewModel> moviesViewModels = movies.stream().map(MovieViewModel::toViewModel).collect(Collectors.toList());
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

//    @GetMapping(
//            value = "/movies/poster",
//            produces = MediaType.IMAGE_PNG_VALUE)
//    public @ResponseBody
//    byte[] getPoster(@RequestParam String posterName) throws IOException {
//        InputStream in = getClass()
//                .getResourceAsStream("/static/posters/" + posterName);
//
//        return StreamUtils.copyToByteArray(in);
//    }
}
