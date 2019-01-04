package ch.moviescore.core.model;

import ch.moviescore.core.data.movie.Movie;
import lombok.Data;

import java.util.List;

/**
 * @author Wetwer
 * @project movie-score
 * @package ch.moviescore.core.model
 * @created 04.01.2019
 **/

@Data
public class MovieListModel {

    private String searchParam;

    private String genre;

    private String orderBy;

    private Integer page;

    private List<Movie> movies;

}
