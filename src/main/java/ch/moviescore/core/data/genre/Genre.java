package ch.moviescore.core.data.genre;

import ch.moviescore.core.data.movie.Movie;
import ch.moviescore.core.data.serie.Serie;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Genre {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    private Movie movie;

    @JsonIgnore
    @ManyToOne
    private Serie serie;

    private String name;

}
