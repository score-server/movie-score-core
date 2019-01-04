package ch.moviescore.core.data.time;

import ch.moviescore.core.data.user.User;
import ch.moviescore.core.data.episode.Episode;
import ch.moviescore.core.data.movie.Movie;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

@Data
@Entity
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Movie movie;

    @ManyToOne
    private Episode episode;

    private Float time;
    private Timestamp timestamp;

}
