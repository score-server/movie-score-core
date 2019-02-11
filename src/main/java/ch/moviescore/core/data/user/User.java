package ch.moviescore.core.data.user;



import ch.moviescore.core.data.activitylog.ActivityLog;
import ch.moviescore.core.data.comment.Comment;
import ch.moviescore.core.data.groupinvite.GroupInvite;
import ch.moviescore.core.data.likes.Likes;
import ch.moviescore.core.data.request.Request;
import ch.moviescore.core.data.session.Session;
import ch.moviescore.core.data.subtitle.Subtitle;
import ch.moviescore.core.data.time.Time;
import ch.moviescore.core.data.timeline.Timeline;
import ch.moviescore.core.data.uploadFile.UploadFile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    private String videoPlayer;

    @JsonIgnore
    private Timestamp lastLogin;

    @JsonIgnore
    private String authKey;

    @Lob
    @JsonIgnore
    private byte[] profileImg;

    @ManyToOne
    private GroupInvite group;

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

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<UploadFile> uploadFiles;

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

}
