package com.filmi3k.movies.controllers;

import com.filmi3k.movies.domain.entities.*;
import com.filmi3k.movies.services.base.ActorService;
import com.filmi3k.movies.services.base.DirectorService;
import com.filmi3k.movies.services.base.MovieService;
import com.filmi3k.movies.services.base.MovieTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
public class AdminController extends BaseController {
    private final String folder = "src/main/resources/static/movies/";
    private final DirectorService directorService;
    private final MovieService movieService;
    private final MovieTypeService movieTypeService;
    private final ActorService actorService;

    @Autowired
    public AdminController(DirectorService directorService, MovieService movieService, MovieTypeService movieTypeService, ActorService actorService) {
        this.directorService = directorService;
        this.movieService = movieService;
        this.movieTypeService = movieTypeService;
        this.actorService = actorService;
    }

    @GetMapping("/admin/add_movie")
    public ModelAndView addMovie(Principal principal, ModelAndView modelAndView) {
        modelAndView.addObject("username",  principal.getName());

        return view("add_movie", modelAndView);
    }

    @PostMapping("/admin/add_movie")
    public void addMovieToDB(@RequestParam Map<String,String> requestParams, @RequestParam("file") MultipartFile file) throws IOException {
        boolean isMovieParametersAlright = false;
        String movieName = "";
        int movieRunningTime = 0;
        String movieYear = "";
        String movieDirector = "";
        String movieGenre = "";
        String movieActor = "";
        if(requestParams.get("movieName") != null && requestParams.get("movieRunningTime") != null
                && requestParams.get("movieYear") != null && requestParams.get("movieDirector") != null
        &&  requestParams.get("movieActor") != null &&  requestParams.get("movieType") != null) {
            Movie movie = this.movieService.findByName(requestParams.get("movieName"));
            if(movie == null) {
                movieName = requestParams.get("movieName");
                isMovieParametersAlright = true;
            }
            MovieType movieType = this.movieTypeService.findByMovieType(requestParams.get("movieType"));
            if(movieType != null) {
                movieGenre = requestParams.get("movieType");
                isMovieParametersAlright = true;
            }
            movieRunningTime= Integer.parseInt(requestParams.get("movieRunningTime"));
            movieYear=requestParams.get("movieYear");

            Director director = this.directorService.findByName(requestParams.get("movieDirector"));
            if(director != null) {
                movieDirector = director.getDirectorName();
                isMovieParametersAlright = true;
            }

            Actor actor = this.actorService.findByName(requestParams.get("movieActor"));
            if(actor != null) {
                movieActor = requestParams.get("movieActor");
                isMovieParametersAlright = true;
            }
        }

        if(file != null && isMovieParametersAlright) {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(folder + file.getOriginalFilename());
            Files.write(path, bytes);

            Movie movie = new Movie();
            movie.setMovieName(movieName);
            movie.setMovieRunningTime(movieRunningTime);
            movie.setMovieYear(movieYear);
            movie.setMovieDirector(directorService.findByName(movieDirector));
            Actor actor = actorService.findByName(movieActor);
            movie.getActors().add(actor);
            MovieType genre = movieTypeService.findByMovieType(movieGenre);
            movie.getMovieTypes().add(genre);
            MovieImage movieImage = new MovieImage(file.getOriginalFilename(), movie);
            movie.setMovieImage(movieImage);
            movieService.add(movie);
        }
    }
}
