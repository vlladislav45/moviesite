package com.jmovies.domain.models.binding;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
@Data
public class SingleMovieBindingModel {
    private String movieName;
    private String summary;
    private int year;
    private String director;
    private String posterName;
    private byte[] posterBytes;
    private List<String> actors = new ArrayList<>();
    private List<String> genres = new ArrayList<>();
    private long duration;
}
