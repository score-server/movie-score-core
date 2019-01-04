package ch.moviescore.core.data.movie;

import ch.moviescore.core.data.genre.Genre;
import ch.moviescore.core.data.time.Time;
import ch.moviescore.core.data.comment.Comment;
import ch.moviescore.core.data.importlog.ImportLog;
import ch.moviescore.core.data.likes.Likes;
import ch.moviescore.core.data.listmovie.ListMovie;
import ch.moviescore.core.data.subtitle.Subtitle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Wetwer
 * @project movie-db
 */
@Data
@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    @JsonIgnore
    @Column(unique = true)
    private String videoPath;

    private String backgroundImg;
    private String caseImg;
    private String trailerKey;
    private Integer tmdbId;
    private String year;
    private String quality;
    private Integer runtime;
    private Double popularity;
    private Double voteAverage;
    private String filetype;
    private Timestamp timestamp;

    @Lob
    @Column
    private String descript;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movie")
    private List<Genre> genres;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movie")
    private List<Comment> comments;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movie")
    private List<ListMovie> listMovies;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movie")
    private List<ImportLog> importLog;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movie")
    private List<Likes> likes;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movie")
    private List<Time> times;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movie")
    private List<Subtitle> subtitles;

    public boolean isBroken() {
        return getQuality().contains(" ");
    }

}
