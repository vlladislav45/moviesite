package com.filmi3k.movies.models.view;

import com.filmi3k.movies.domain.entities.Actor;
import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.domain.entities.MovieGenre;
import org.springframework.util.StreamUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class MovieViewModel {
    private String movieName;
    private int year;
    private List<String> actors;
    private List<String> genres;
    private byte[] moviePoster;
    private String posterName;

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public byte[] getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(byte[] moviePoster) {
        this.moviePoster = moviePoster;
    }

    public static MovieViewModel toViewModel(Movie movie) {
        MovieViewModel viewModel = new MovieViewModel();
        viewModel.actors = movie.getActors().stream().map(Actor::getActorName).collect(Collectors.toList());
        viewModel.movieName = movie.getMovieName();
        viewModel.year = movie.getMovieYear();
        viewModel.genres = movie.getMovieGenres().stream().map(MovieGenre::getMovieGenreName).collect(Collectors.toList());

        //viewModel.posterName = movie.getMovieImage().getMovieImageName();
        InputStream in = MovieViewModel.class.getResourceAsStream("/static/posters/" + movie.getPoster().getPosterName());

        try {
            viewModel.moviePoster = StreamUtils.copyToByteArray(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return viewModel;
    }

    public static void compraseImage(String imagePath, OutputStream outputStream) throws IOException {
        File imageFile = new File("YOUR_IMAGE.jpg");

        InputStream inputStream = new FileInputStream(imageFile);

        float imageQuality = 0.3f;

        //Create the buffered image
        BufferedImage bufferedImage = ImageIO.read(inputStream);

        //Get image writers
        Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName("jpg");

        if (!imageWriters.hasNext())
            throw new IllegalStateException("Writers Not Found!!");

        ImageWriter imageWriter = (ImageWriter) imageWriters.next();
        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
        imageWriter.setOutput(imageOutputStream);

        ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();

        //Set the compress quality metrics
        imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        imageWriteParam.setCompressionQuality(imageQuality);

        //Created image
        imageWriter.write(null, new IIOImage(bufferedImage, null, null), imageWriteParam);

        // close all streams
        inputStream.close();
        outputStream.close();
        imageOutputStream.close();
        imageWriter.dispose();
    }
}
