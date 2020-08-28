package com.jmovies.controllers;

import com.jmovies.domain.models.binding.SingleMovieBindingModel;
import com.jmovies.services.base.MovieService;
import com.jmovies.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

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

        URL url = getClass().getResource(Config.BASE_DIR + "/posters/");
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
