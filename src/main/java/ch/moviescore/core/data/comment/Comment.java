package ch.moviescore.core.data.comment;

import ch.moviescore.core.data.movie.Movie;
import ch.moviescore.core.data.user.User;
import ch.moviescore.core.data.episode.Episode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * @author Wetwer
 * @project movie-db
 */
@Data
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    private Movie movie;

    @JsonIgnore
    @ManyToOne
    private Episode episode;

    @NotNull
    @ManyToOne
    private User user;

    @Lob
    @NotNull
    private String comment;


}
