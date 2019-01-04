package ch.moviescore.core.data.likes;

import ch.moviescore.core.data.movie.Movie;
import ch.moviescore.core.data.user.User;
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
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Movie movie;

//    @ManyToOne
//    private Episode episode;

}
