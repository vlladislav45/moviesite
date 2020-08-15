package com.filmi3k.movies.controllers;

import com.filmi3k.movies.domain.models.binding.SingleMovieBindingModel;
import com.filmi3k.movies.services.base.MovieService;
import com.filmi3k.movies.services.base.StorageService;
import com.filmi3k.movies.utils.JSONparser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static com.filmi3k.movies.config.Config.BASE_DIR;

@RestController
public class AdminController {
    private final MovieService movieService;

    @Autowired
    public AdminController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/admin/movie/add")
    public ResponseEntity<?> addMovie(@RequestBody SingleMovieBindingModel singleMovieModel) {
        //movieService.delete(movieService.findByName("Fast and furious 11"));
        Map<String, String> errors = movieService.checkMovieFields(singleMovieModel);
        if(errors.size() > 0) { // If exists errors
            return ResponseEntity.ok(errors);
        }

        URL url = getClass().getResource(BASE_DIR + "/posters/");
        try (FileOutputStream fos = new FileOutputStream(url.getPath() + "/" + singleMovieModel.getPosterName())) {
            fos.write(singleMovieModel.getPosterBytes());
        }catch(FileNotFoundException fileNotFoundException) {
            return ResponseEntity.ok(Map.of("error", "Could not write your file" + fileNotFoundException));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.ok(Map.of("error", e));
        }
        movieService.add(singleMovieModel);
        return ResponseEntity.ok(Map.of("success", singleMovieModel.getMovieName() + " is added successfully"));
    }
}
