package ch.moviescore.core.model;

import ch.moviescore.core.data.movie.Movie;
import ch.moviescore.core.data.updatelog.UpdateLog;
import lombok.Data;

import java.util.List;

/**
 * @author Wetwer
 * @project movie-score-core
 * @package ch.moviescore.core.model
 * @created 08.01.2019
 **/

@Data
public class PageInfoModel {

    private Integer ammountMovies;

    private Integer ammountSeries;

    private Integer ammountEpisodes;

    private List<Movie> latestMovies;

    private List<UpdateLog> updateLogs;

}
