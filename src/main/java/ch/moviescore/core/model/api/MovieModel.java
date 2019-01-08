package ch.moviescore.core.model.api;

import ch.moviescore.core.data.movie.Movie;
import lombok.Data;

import java.util.List;

/**
 * @author Wetwer
 * @project movie-score-core
 * @package ch.moviescore.core.model.api
 * @created 08.01.2019
 **/

@Data
public class MovieModel {

    private Movie movie;

    private Float time;

    private Boolean liked;

    private List<Movie> similarMovies;


}
