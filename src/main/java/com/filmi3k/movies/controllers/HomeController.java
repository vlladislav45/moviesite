package com.filmi3k.movies.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Vladislav Enev
 */
@Controller
public class HomeController {

    @GetMapping("/index")
    public String home() {
        return "index";
    }
}
