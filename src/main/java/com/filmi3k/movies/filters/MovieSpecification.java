package com.filmi3k.movies.filters;

import com.filmi3k.movies.domain.entities.Movie;
import com.filmi3k.movies.domain.entities.MovieGenre;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.*;

public class MovieSpecification {
    public static Specification<Movie> withGenres(List<String> genres) {
        return (root, query, builder) -> {
            if (genres.isEmpty())
                return builder.conjunction();

            query.distinct(true);
            query.groupBy(root.get("movieId"));
            query.having(builder.equal(builder.count(root.get("movieId")), (long) genres.size()));

            return root.join("movieGenres").get("movieGenreName").in(genres);
        };
    }

    public static Specification<Movie> withNameLike(String name) {
        return (root, query, builder) -> builder.like(root.get("movieName"), "%" + name + "%");

    }

    public static Specification<Movie> withYear(int year) {
        return (root, query, builder) -> builder.equal(root.get("movieYear"), year);
    }
}
