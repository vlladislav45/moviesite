package com.filmi3k.movies.controllers;

import com.filmi3k.movies.services.base.MovieService;
import com.filmi3k.movies.services.base.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.security.Principal;

/**
 * @author Vladislav Enev
 */
@Controller
public class HomeController extends BaseController {
    private final MovieService movieService;
    private final UserService userService;

    @Autowired
    public HomeController(MovieService movieService, UserService userService) {
        this.movieService = movieService;
        this.userService = userService;
    }


    @GetMapping("/index")
    public ModelAndView home(HttpSession session, ModelAndView modelAndView, Principal principal) {
        //Return movies to the home page
        session.setAttribute("user", this.userService.getUserByUsername(principal.getName()));
        modelAndView.addObject("movies", this.movieService.findAll());

        return view("index", modelAndView);
    }
}
