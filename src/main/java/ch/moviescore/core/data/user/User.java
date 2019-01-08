package ch.moviescore.core.data.user;

import ch.moviescore.core.data.time.Time;
import ch.moviescore.core.data.activitylog.ActivityLog;
import ch.moviescore.core.data.comment.Comment;
import ch.moviescore.core.data.groupinvite.GroupInvite;
import ch.moviescore.core.data.likes.Likes;
import ch.moviescore.core.data.request.Request;
import ch.moviescore.core.data.session.Session;
import ch.moviescore.core.data.subtitle.Subtitle;
import ch.moviescore.core.data.timeline.Timeline;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Wetwer
 * @project movie-db
 */
@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    @Size(min = 3, max = 20)
    private String name;

    @NotNull
    @JsonIgnore
    private String passwordSha;

    @ManyToOne
    private GroupInvite group;

    private Integer role;

    @JsonIgnore
    private String videoPlayer;

    @JsonIgnore
    private Timestamp lastLogin;

    @JsonIgnore
    private String authKey;

    @Lob
    @JsonIgnore
    private byte[] profileImg;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Request> requests;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Comment> comments;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Timeline> timelines;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Likes> likes;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<ActivityLog> activityLogs;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Time> times;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Session> sessions;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Subtitle> subtitles;

    public boolean isAdmin() {
        if (role == 2) {
            return true;
        }
        return false;
    }

}
