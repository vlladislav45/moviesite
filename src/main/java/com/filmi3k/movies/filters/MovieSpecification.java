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
//            SELECT distinct(l.movie_name)
//                    FROM movie l
//            JOIN movies_genres a ON a.movie_id = l.movie_id
//            JOIN movie_genre t ON a.movie_genre_id = t.genre_id
//            WHERE t.movie_genre_name IN ('Horror', 'Scientist')
//            GROUP BY l.movie_id
//            HAVING COUNT(*) = 2;
            query.having(builder.equal(builder.count(root.get("movieId")), (long) genres.size()));

            return root.join("movieGenres").get("movieGenreName").in(genres);
        };
    }

    public static Specification<Movie> withNameLike(String name) {
        return (root, query, builder) -> builder.like(root.get("movieName"), "%" + name + "%");
    }
}
