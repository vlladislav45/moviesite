package com.filmi3k.movies.controllers;

import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.domain.entities.User;
import com.filmi3k.movies.models.binding.AuthenticationRequestBindingModel;
import com.filmi3k.movies.models.binding.AuthenticationResponseBindingModel;
import com.filmi3k.movies.models.binding.UserRegisterBindingModel;
import com.filmi3k.movies.models.view.UserRatingViewModel;
import com.filmi3k.movies.services.base.MovieService;
import com.filmi3k.movies.services.base.UserService;
import com.filmi3k.movies.utils.JSONparser;
import com.filmi3k.movies.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    private final UserService userService;
    private final MovieService movieService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    public UserController(UserService userService, MovieService movieService) {
        this.userService = userService;
        this.movieService = movieService;
    }

    @PostMapping("/register_user")
    public ResponseEntity<Map<String,Object>> register(@RequestBody UserRegisterBindingModel userRegisterBindingModel) {
        Map<String,Object> response = new HashMap<>();
        if(!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            response.put("error", "The password does not match the confirmation password");
            return ResponseEntity.ok().body(response);
        }else {
            if(userService.getUserByUsername(userRegisterBindingModel.getUsername()) || userService.getUserByEmail(userRegisterBindingModel.getEmail())) {
                response.put("error", "This user is already registered");
                return ResponseEntity.ok().body(response);
            }
            this.userService.add(userRegisterBindingModel);
        }
        response.put("success", "Your successfully register an account");
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/authentication")
    public ResponseEntity<?> authenticationToken(@RequestBody AuthenticationRequestBindingModel authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );

        final UserDetails userDetails = userService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok().body(new AuthenticationResponseBindingModel(jwt));
    }

    @GetMapping("/register/userAvailable/{username}")
    public ResponseEntity<Map<String,Object>> getUsernameIfExist(@PathVariable String username) {
        Map<String,Object> response = new HashMap<>();
        if(userService.getUserByUsername(username)) {
            response.put("isUsernameExist", "true");
            return ResponseEntity.ok().body(response);
        }
        response.put("isUsernameExist", "false");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/user/isRated")
    public ResponseEntity<Map<String,Object>> isRated(@RequestParam int userId, @RequestParam int movieId) {
        Map<String,Object> response = new HashMap<>();
        if (userService.getById(userId) != null && movieService.findById(movieId) != null) { //check if the user already exists at the system
            User user = userService.getById(userId);
            Movie movie = movieService.findById(movieId);

            if (userService.checkRating(user, movie) != null) { // check if the user has voted for the movie
                response.put("userRating",JSONparser.toJson(UserRatingViewModel.toViewModel(userService.checkRating(user, movie)).getMovieRating()));
                response.put("comment",UserRatingViewModel.toViewModel(userService.checkRating(user, movie)).getComment());
                return ResponseEntity.ok().body(response);
            }
        }
        response.put("error", "null");
        return ResponseEntity.ok().body(response);
    }
}
