package ch.moviescore.core.model.api;

import ch.moviescore.core.data.comment.Comment;
import ch.moviescore.core.data.episode.Episode;
import lombok.Data;

import java.util.List;

/**
 * @author Wetwer
 * @project movie-score
 * @package ch.moviescore.core.model
 * @created 04.01.2019
 **/

@Data
public class EpisodeModel {

    private Episode episode;

    private Float time;

    private Episode nextEpisode;

    private List<Comment> comments;

}
