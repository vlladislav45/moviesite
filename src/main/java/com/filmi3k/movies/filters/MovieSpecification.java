package com.filmi3k.movies.filters;

import com.filmi3k.movies.domain.entities.Movie;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.*;

public class MovieSpecification {
    public static Specification<Movie> countWithGenres(List<String> genres) {
        return (root, query, builder) -> {
/**    SELECT SUM(mycount)
//            FROM
//                    (SELECT COUNT(DISTINCT(movie.movie_id)) AS mycount
//                            FROM movie
//                            INNER JOIN movies_genres ON movies_genres.movie_id = movie.movie_id
//                            WHERE movies_genres.movie_genre_id IN (1,2)
//                            GROUP BY movie.movie_id
//                            HAVING COUNT(movies_genres.movie_genre_id) = 2
                    ) as R; */
            query.distinct(true);
            Predicate predicate = builder.conjunction();

            Subquery<Long> subQuery = query.subquery(Long.class);
            Root<Movie> subRoot = subQuery.from(Movie.class);

            //subQuery.select(builder.countDistinct(subRoot));
            subQuery.select(subRoot.get("movieId"));
            subQuery.groupBy(subRoot.get("movieId"));
            Predicate subPredicate = subRoot.join("movieGenres").get("movieGenreName").in(genres);
            subQuery.having(builder.equal(builder.count(root.get("movieId")), (long) genres.size()));

            //Predicate subPredicate = builder.in(subRoot.get("movieGenreName"), genres);
            subQuery.where(subPredicate);

            //query.where(builder.in(root.get("movieId")).value(subQuery));
            Expression<String> exp = root.get("movieId");
            Predicate p1 = exp.in(subQuery);
            predicate.getExpressions().add(p1);
            return predicate;
        };
    }

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

    public static Specification<Movie> withFilter(MovieFilters movieFilters) {
        return (root, query, builder) -> {
            if(movieFilters.getYear() != 0)
                return builder.equal(root.get("movieYear"), movieFilters.getYear());
            else if(!movieFilters.getSearch().isEmpty() && movieFilters.getSearch() != null)
                return builder.like(root.get("movieName"), "%" + movieFilters.getSearch() + "%");
            return null;
        };

    }
}
