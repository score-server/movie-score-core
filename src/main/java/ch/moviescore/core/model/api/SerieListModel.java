package ch.moviescore.core.model.api;

import ch.moviescore.core.data.serie.Serie;
import lombok.Data;

import java.util.List;

/**
 * @author Wetwer
 * @project movie-score
 * @package ch.moviescore.core.model
 * @created 04.01.2019
 **/

@Data
public class SerieListModel {

    private String searchParam;

    private String genre;

    private List<Serie> series;

}
