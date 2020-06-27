package com.filmi3k.movies.controllers;

import com.filmi3k.movies.domain.entities.*;
import com.filmi3k.movies.services.base.ActorService;
import com.filmi3k.movies.services.base.DirectorService;
import com.filmi3k.movies.services.base.MovieService;
import com.filmi3k.movies.services.base.MovieGenreService;
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
import java.util.Map;

@Controller
public class AdminController extends BaseController {
    private final String folder = "src/main/resources/static/posters/";
    private final DirectorService directorService;
    private final MovieService movieService;
    private final MovieGenreService movieGenreService;
    private final ActorService actorService;

    @Autowired
    public AdminController(DirectorService directorService, MovieService movieService, MovieGenreService movieGenreService, ActorService actorService) {
        this.directorService = directorService;
        this.movieService = movieService;
        this.movieGenreService = movieGenreService;
        this.actorService = actorService;
    }

//    @PostMapping("/admin/add_movie")
//    public void addMovieToDB(@RequestParam Map<String,String> requestParams, @RequestParam("file") MultipartFile file) throws IOException {
//        boolean isMovieParametersAlright = false;
//        String movieName = "";
//        int movieRunningTime = 0;
//        int movieYear = 0;
//        String movieDirector = "";
//        String movieGenre = "";
//        String movieActor = "";
//        if(requestParams.get("movieName") != null && requestParams.get("movieRunningTime") != null
//                && requestParams.get("movieYear") != null && requestParams.get("movieDirector") != null
//        &&  requestParams.get("movieActor") != null &&  requestParams.get("movieType") != null) {
//            Movie movie = this.movieService.findByName(requestParams.get("movieName"));
//            if(movie == null) {
//                movieName = requestParams.get("movieName");
//                isMovieParametersAlright = true;
//            }
//            MovieGenre movieType = this.movieGenreService.findByMovieType(requestParams.get("movieType"));
//            if(movieType != null) {
//                movieGenre = requestParams.get("movieType");
//                isMovieParametersAlright = true;
//            }
//            movieRunningTime= Integer.parseInt(requestParams.get("movieRunningTime"));
//            movieYear=Integer.parseInt(requestParams.get("movieYear"));
//
//            Director director = this.directorService.findByName(requestParams.get("movieDirector"));
//            if(director != null) {
//                movieDirector = director.getDirectorName();
//                isMovieParametersAlright = true;
//            }
//
//            Actor actor = this.actorService.findByName(requestParams.get("movieActor"));
//            if(actor != null) {
//                movieActor = requestParams.get("movieActor");
//                isMovieParametersAlright = true;
//            }
//        }
//
//        if(file != null && isMovieParametersAlright) {
//            byte[] bytes = file.getBytes();
//            Path path = Paths.get(folder + file.getOriginalFilename());
//            Files.write(path, bytes);
//
//            Movie movie = new Movie();
//            movie.setMovieName(movieName);
//            movie.setMovieViews(movieRunningTime);
//            movie.setMovieYear(movieYear);
//            movie.setMovieDirector(directorService.findByName(movieDirector));
//            Actor actor = actorService.findByName(movieActor);
//            movie.getActors().add(actor);
//            MovieGenre genre = movieGenreService.findByMovieType(movieGenre);
//            movie.getMovieGenres().add(genre);
//            Poster poster = new Poster(file.getOriginalFilename(), movie);
//            movie.setPoster(poster);
//            movieService.add(movie);
//        }
//    }
}
