package com.filmi3k.movies.controllers;

import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.domain.entities.User;
import com.filmi3k.movies.domain.entities.UsersRating;
import com.filmi3k.movies.models.binding.UserRatingBindingModel;
import com.filmi3k.movies.models.binding.UserRegisterBindingModel;
import com.filmi3k.movies.models.view.UserRatingViewModel;
import com.filmi3k.movies.services.base.MovieService;
import com.filmi3k.movies.services.base.UserService;
import com.filmi3k.movies.utils.JSONparser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.filmi3k.movies.config.Config.MAX_VOTE;
import static com.filmi3k.movies.config.Config.MIN_VOTE;

@RestController
public class UserController {
    private final UserService userService;
    private final MovieService movieService;

    @Autowired
    public UserController(UserService userService, MovieService movieService) {
        this.userService = userService;
        this.movieService = movieService;
    }

    @PostMapping("/register_user")
    public ResponseEntity<String> register(@RequestBody UserRegisterBindingModel userRegisterBindingModel) {
        if(!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            return ResponseEntity.ok().body("The password does not match the confirmation password");
        }else {
            if(userService.getUserByUsername(userRegisterBindingModel.getUsername()) || userService.getUserByEmail(userRegisterBindingModel.getEmail()))
                return ResponseEntity.ok().body("This user is already registered");
            this.userService.add(userRegisterBindingModel);
        }
        return ResponseEntity.ok().body("Your successfully register an account");
    }

    @GetMapping("/register/userAvailable/{username}")
    public ResponseEntity<String> getUsernameIfExist(@PathVariable String username) {
        if(userService.getUserByUsername(username)) {
            return ResponseEntity.ok()
                    .body("true");
        }
        return ResponseEntity.ok()
                .body("false");
    }

    @GetMapping("/user/isRated")
    public ResponseEntity<String> isRated(@RequestParam int userId, @RequestParam int movieId) {
        if (userService.getById(userId) != null && movieService.findById(movieId) != null) { //check if the user already exists at the system
            User user = userService.getById(userId);
            Movie movie = movieService.findById(movieId);

            if (userService.checkRating(user, movie) != null) { // check if the user has voted for the movie
                return ResponseEntity.ok().body(JSONparser.toJson(UserRatingViewModel.toViewModel(userService.checkRating(user, movie))));
            }
        }
        return ResponseEntity.ok().body("null");
    }
}
