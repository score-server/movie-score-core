package ch.moviescore.core.data.comment;

import ch.moviescore.core.data.DaoInterface;
import ch.moviescore.core.data.movie.Movie;
import ch.moviescore.core.data.user.User;
import ch.moviescore.core.data.episode.Episode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentDao implements DaoInterface<Comment> {

    private CommentRepository commentRepository;

    public CommentDao(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment getById(Long id) {
        return commentRepository.getOne(id);
    }

    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    public void createMovieComment(User user, Movie movie, String commentParam) {
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setMovie(movie);
        comment.setComment(commentParam);
        save(comment);
    }

    public void createEpisodeComment(User user, Episode episode, String commentParam) {
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setEpisode(episode);
        comment.setComment(commentParam);
        save(comment);
    }
}
