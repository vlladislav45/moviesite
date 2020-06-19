package com.filmi3k.movies.controllers;

import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.models.view.MovieViewModel;
import com.filmi3k.movies.services.base.MovieService;
import com.filmi3k.movies.services.base.UserService;
import com.filmi3k.movies.utils.JSONparser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import static com.filmi3k.movies.utils.CompraseImage.compraseImage;

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

    @GetMapping("/movies/poster/{posterName}")
    public ResponseEntity<byte[]> getPoster(@PathVariable String posterName) throws IOException {
        URL url = getClass().getResource("/static/posters/" + posterName);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (InputStream inputStream = url.openStream()) {
            int n = 0;
            byte [] buffer = new byte[ 1024 ];
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
        }

//        OutputStream outputStream = url.openStream();
//        compraseImage(url.toString(), outputStream);

        ResponseEntity<byte[]> retVal = ResponseEntity.ok()
                .contentType(MediaType.valueOf(MediaType.IMAGE_JPEG_VALUE))
                .body(output.toByteArray());
        return retVal;
    }

    @GetMapping("/movies/single/{id}")
    @ResponseBody
    public ResponseEntity<Movie> getMovieInformation(@PathVariable int id) {
        ResponseEntity<Movie> movie = ResponseEntity.ok()
                .body(movieService.findById(id));
        return movie;
    }

}
