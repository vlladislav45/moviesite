package com.filmi3k.movies.services.impl;

import com.filmi3k.movies.domain.entities.*;
import com.filmi3k.movies.domain.models.binding.SingleMovieBindingModel;
import com.filmi3k.movies.domain.models.binding.UserRatingBindingModel;
import com.filmi3k.movies.repository.api.*;
import com.filmi3k.movies.services.base.MovieService;
import com.filmi3k.movies.utils.Math;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final UsersRatingRepository usersRatingRepository;
    private final DirectorRepository directorRepository;
    private final ActorRepository actorRepository;
    private final MovieGenreRepository movieGenreRepository;
    private final PosterRepository posterRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, UsersRatingRepository usersRatingRepository,
                            DirectorRepository directorRepository, ActorRepository actorRepository,
                            MovieGenreRepository movieGenreRepository, PosterRepository posterRepository, ModelMapper modelMapper) {
        this.movieRepository = movieRepository;
        this.usersRatingRepository = usersRatingRepository;
        this.directorRepository = directorRepository;
        this.actorRepository = actorRepository;
        this.movieGenreRepository = movieGenreRepository;
        this.posterRepository = posterRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void add(SingleMovieBindingModel singleMovieBindingModel) {
        Movie movie = new Movie();
        movie.setMovieName(singleMovieBindingModel.getMovieName());
        movie.setMovieYear(singleMovieBindingModel.getYear());
        movie.setMovieSummary(singleMovieBindingModel.getSummary());
        movie.setMovieDuration(singleMovieBindingModel.getDuration());
        movie.setMovieDirector(directorRepository.findByDirectorName(singleMovieBindingModel.getDirector()));
        movie.setActors(new HashSet<>(actorRepository.findByActorNameIn(singleMovieBindingModel.getActors())));
        movie.setMovieGenres(new HashSet<>(movieGenreRepository.findByMovieGenreNameIn(singleMovieBindingModel.getGenres())));
        movie.setPoster(null);

        this.movieRepository.saveAndFlush(movie);
        //Create the relationship between Movie and Poster
        this.posterRepository.saveAndFlush(new Poster(singleMovieBindingModel.getPosterName(), movie));
    }

    @Override
    public List<String> checkMovieFields(SingleMovieBindingModel singleMovieBindingModel) {
        List<String> errors = new ArrayList<>();
        // If movie exists with this name
        if(movieRepository.findByMovieName(singleMovieBindingModel.getMovieName()) != null) {
            errors.add("* This movie already exists, try again");
            return errors;
        }
        //Check every one parameter
        if(singleMovieBindingModel.getMovieName().isEmpty()) {
            errors.add("* Require movie name");
        }
        if(singleMovieBindingModel.getSummary().isEmpty()) {
            errors.add("* Require summary");
        }
        if(singleMovieBindingModel.getYear() <= 0) {
            errors.add("* Require year");
        }
        if(singleMovieBindingModel.getDirector().isEmpty()) {
            errors.add("* Require director");
        }else {
            if(directorRepository.findByDirectorName(singleMovieBindingModel.getDirector()) == null) {
                directorRepository.saveAndFlush(new Director(singleMovieBindingModel.getDirector()));
            }
        }
        if(singleMovieBindingModel.getPosterName().isEmpty()) {
            errors.add("* Require poster");
        }else{
            //If exists poster with this name
            if(posterRepository.findByPosterName(singleMovieBindingModel.getPosterName()) != null)
                errors.add("* There is a poster with such a name already");
        }
        if(singleMovieBindingModel.getActors().size() <= 0) {
            errors.add("* Require actor/s");
        }else {
            List<String> actors = actorRepository.findByActorNameIn(singleMovieBindingModel.getActors()).stream().map(Actor::getActorName).collect(Collectors.toList());

            //Temp array
            List<String> tempActors = new ArrayList<>(singleMovieBindingModel.getActors());
            tempActors.removeAll(actors); // Remove existing actors

            if(tempActors.size() > 0) { // If exists missing actors
                for (String actor : tempActors) {
                    //System.out.println("Missing actors " + actor);
                    //Add to the DB
                    //ONLY FOR ADMINS
                    actorRepository.saveAndFlush(new Actor(actor));
                }
            }
        }
        if(singleMovieBindingModel.getGenres().size() <= 0) {
            errors.add("* Require genres");
        }else {
            List<String> genres = movieGenreRepository.findByMovieGenreNameIn(singleMovieBindingModel.getGenres()).stream().map(MovieGenre::getMovieGenreName).collect(Collectors.toList());

            //Temp array
            List<String> tempGenres = new ArrayList<>(singleMovieBindingModel.getGenres());
            tempGenres.removeAll(genres); // Remove existing genders

            if(tempGenres.size() > 0) { // If exists missing genders
                for (String genre : tempGenres) {
                    //System.out.println("Missing gender " + gender);
                    //Show missing genders
                    //ONLY FOR ADMINS
                    errors.add("Missing genre " + genre);
                }
            }
        }
        if(singleMovieBindingModel.getDuration() <= 0) {
            errors.add("* Require movie duration");
        }
        return errors;
    }

    @Override
    public Movie findById(int id) {
        Optional<Movie> movie = movieRepository.findById(id);
        return movie.orElse(null);
    }

    @Override
    public Page<Movie> findAll(Specification<Movie> specification, Pageable pageable) {
        return movieRepository.findAll(specification,pageable);
    }

    @Override
    public long count(Specification<Movie> specification) {
        return movieRepository.count(specification);
    }

    @Override
    public double updateRating(UserRatingBindingModel userRatingBindingModel) {
        double sum = usersRatingRepository.sumAllUsersRatingByMovie(userRatingBindingModel.getMovieId());
        int count = usersRatingRepository.countUsersRatingByMovieId(userRatingBindingModel.getMovieId());

        double average = 0.0;
        if(count != 0)
            average = sum / count;

        Movie movie = this.findById(userRatingBindingModel.getMovieId());
        movie.setMovieRating(Math.round(average,2));
        movieRepository.saveAndFlush(movie);

        return movie.getMovieRating();
    }

    @Override
    public Movie findByName(String movieName) {
        return movieRepository.findByMovieName(movieName);
    }

    @Override
    public void delete(Movie movie) { movieRepository.delete(movie); }
}
