package ch.moviescore.core.data.listmovie;

import ch.moviescore.core.data.movie.Movie;
import ch.moviescore.core.data.timeline.Timeline;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author Wetwer
 * @project movie-db
 */
@Data
@Entity
public class ListMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Movie movie;

    @ManyToOne
    private Timeline timeline;

    private Integer place;
}
