package com.jmovies.controllers;

import com.jmovies.domain.entities.Movie;
import com.jmovies.domain.entities.User;
import com.jmovies.domain.entities.UserRole;
import com.jmovies.domain.models.binding.SingleMovieBindingModel;
import com.jmovies.services.base.MovieService;
import com.jmovies.config.Config;
import com.jmovies.services.base.StorageService;
import com.jmovies.services.base.UserService;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static com.jmovies.config.Config.BASE_DIR;

@RestController
public class AdminController {
    private final MovieService movieService;
    private final UserService userService;
    private final StorageService storageService;

    @Autowired
    public AdminController(MovieService movieService, UserService userService, StorageService storageService) {
        this.movieService = movieService;
        this.userService = userService;
        this.storageService = storageService;
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

    /**
     * Add movie file by chunks we expect a file with file name in this format:
     * movieName_userName_chunkNumber
     * @param file the file to upload
     * @return 200OK if success else TODO: What other errors?
     */
    @PostMapping("/admin/movie/upload")
    public ResponseEntity<?> addMovieFile(MultipartFile file) {
        String[] fileNameTokens = file.getOriginalFilename().
                split("_");
        String movieName = fileNameTokens[0];
        String uploadedBy = fileNameTokens[1];
        int chunkNumber = Integer.parseInt(fileNameTokens[2]);
        boolean isSuccess = storageService.storeInChunks(file, movieName, chunkNumber == 0);
        return isSuccess
                ? ResponseEntity.ok(Map.of("success", "chunk uploaded successfully"))
                : ResponseEntity.unprocessableEntity().body(Map.of("error", "Something went wrong"));
    }

    @GetMapping("/admin/movie/delete")
    public ResponseEntity<?> deleteMovie(@RequestParam int movieId) {
        Movie movie = movieService.findById(movieId);

        //Get user by token username
        User user = userService.getByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName());

        if(movie != null && user != null) {
            List<String> currentUserAuthorities = user.getAuthorities()
                    .stream().map(UserRole::getAuthority).collect(Collectors.toList());

            if(currentUserAuthorities.contains("ADMIN")) {
                URL url = getClass().getResource(BASE_DIR + "/posters/" + movie.getPoster().getPosterName());
                /*
                In this case there will be no poster in the movie
                Just delete from database
                 */
                if(url == null) {
                    movieService.delete(movie);
                    return ResponseEntity.ok().body(Map.of("success", "Movie was deleted"));
                }
                File poster = new File(url.getFile());

                boolean successDelete = poster.delete();

                if(successDelete) {
                    //Delete from database and from system hard drive
                    movieService.delete(movie);
                    return ResponseEntity.ok().body(Map.of("success", "Movie was deleted"));
                }
            }
        }
        return ResponseEntity.ok().body(Map.of("error", "Something is wrong, please try again"));
    }
}
