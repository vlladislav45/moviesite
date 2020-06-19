package com.filmi3k.movies.controllers;

import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.services.base.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;

/**
 * @author Vladislav Enev
 */
@Controller
public class HomeController extends BaseController {
    private final MovieService movieService;

    @Autowired
    public HomeController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/index")
    public ModelAndView movies(ModelMap map) {
        Set<Movie> movies = movieService.findAll();
        map.addAttribute("movies", movies);

        return view("index");
    }

}
