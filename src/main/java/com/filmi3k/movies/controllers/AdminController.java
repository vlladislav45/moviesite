package com.filmi3k.movies.controllers;

import com.filmi3k.movies.domain.models.binding.SingleMovieBindingModel;
import com.filmi3k.movies.services.base.MovieService;
import com.filmi3k.movies.services.base.StorageService;
import com.filmi3k.movies.utils.BASE64DecodedMultipartFile;
import com.filmi3k.movies.utils.JSONparser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static com.filmi3k.movies.config.Config.BASE_DIR;

@RestController
public class AdminController {
    private final MovieService movieService;
    private final StorageService storageService;

    @Autowired
    public AdminController(MovieService movieService, StorageService storageService) {
        this.movieService = movieService;
        this.storageService = storageService;
    }

    @PostMapping("/admin/movie/add")
    public ResponseEntity<?> addMovie(@RequestBody SingleMovieBindingModel singleMovieModel) {
        BASE64DecodedMultipartFile base64DecodedMultipartFile = new BASE64DecodedMultipartFile(singleMovieModel.getPosterBytes(), singleMovieModel.getPosterName());

        List<String> errors = movieService.checkMovieFields(singleMovieModel);
        if(errors.size() > 0) { // If exists errors
            String resp = "[";
            String errorJson = errors.stream().map(JSONparser::toJson).collect(Collectors.joining(","));
            resp += errorJson + "]";
            return ResponseEntity.ok(resp);
        }

        String path = "static/posters/";
        boolean isSuccess = this.storageService.store(base64DecodedMultipartFile ,path);
        if(!isSuccess) {
            return ResponseEntity.ok(Map.of("error", "Could not write your file"));
        }
        movieService.add(singleMovieModel);
        return ResponseEntity.ok(Map.of("success", singleMovieModel.getMovieName() + " is added successfully"));
    }
}
