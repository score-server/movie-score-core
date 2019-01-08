package ch.moviescore.core.controller;

import ch.moviescore.core.data.comment.CommentDao;
import ch.moviescore.core.data.episode.Episode;
import ch.moviescore.core.data.episode.EpisodeDao;
import ch.moviescore.core.data.movie.Movie;
import ch.moviescore.core.data.movie.MovieDao;
import ch.moviescore.core.data.user.User;
import ch.moviescore.core.service.ActivityService;
import ch.moviescore.core.service.auth.UserAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Wetwer
 * @project movie-db
 */

@Controller
@RequestMapping("comment")
public class CommentController {

    private MovieDao movieDao;
    private EpisodeDao episodeDao;
    private CommentDao commentDao;

    private UserAuthService userAuthService;
    private ActivityService activityService;

    public CommentController(MovieDao movieDao, EpisodeDao episodeDao, CommentDao commentDao,
                             UserAuthService userAuthService, ActivityService activityService) {
        this.movieDao = movieDao;
        this.episodeDao = episodeDao;
        this.commentDao = commentDao;
        this.userAuthService = userAuthService;
        this.activityService = activityService;
    }

    @PostMapping("add/movie")
    public String addCommentForMovie(@RequestParam("movieId") Long movieId,
                                     @RequestParam("comment") String commentParam,
                                     HttpServletRequest request) {

        if (userAuthService.isUser(request)) {
            User user = userAuthService.getUser(request).getUser();
            Movie movie = movieDao.getById(movieId);

            commentDao.createMovieComment(user, movie, commentParam);

            activityService.log(user.getName() + " created Comment on Movie " + movie.getTitle());
            return "redirect:/movie/" + movieId;
        } else {
            return "AUTH_ERROR";
        }
    }

    @PostMapping("add/episode")
    public String addCommentForEpisode(@RequestParam("episodeId") Long episodeId,
                                       @RequestParam("comment") String commentParam,
                                       HttpServletRequest request) {
        if (userAuthService.isUser(request)) {
            User user = userAuthService.getUser(request).getUser();
            Episode episode = episodeDao.getById(episodeId);

            commentDao.createEpisodeComment(user, episode, commentParam);

            activityService.log(user.getName() + " created Comment on Episode " + episode.getFullTitle());
            return "redirect:/episode/" + episodeId;
        } else {
            return "AUTH_ERROR";
        }
    }
}
