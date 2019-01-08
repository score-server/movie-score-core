package ch.moviescore.core.controller;

import ch.moviescore.core.data.episode.EpisodeDao;
import ch.moviescore.core.data.movie.MovieDao;
import ch.moviescore.core.service.MultipartFileSender;
import ch.moviescore.core.service.auth.UserAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Paths;

/**
 * @author Wetwer
 * @project movie-db
 */
@RestController
@RequestMapping("video")
public class VideoController {

    private MovieDao movieDao;
    private EpisodeDao episodeDao;

    private UserAuthService userAuthService;

    public VideoController(MovieDao movieDao, EpisodeDao episodeDao,
                           UserAuthService userAuthService) {
        this.movieDao = movieDao;
        this.episodeDao = episodeDao;
        this.userAuthService = userAuthService;
    }

    @GetMapping(value = "/movie/{movieId}")
    public void getMovie(@PathVariable("movieId") Long movieId,
                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (userAuthService.isUser(request)) {
            userAuthService.log(this.getClass(), request);
            MultipartFileSender.fromPath(Paths.get(movieDao.getById(movieId).getVideoPath()))
                    .with(request)
                    .with(response)
                    .serveResource();
        }
    }

    @GetMapping(value = "/episode/{episodeId}")
    public void getEpisode(@PathVariable("episodeId") Long episodeId,
                           HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (userAuthService.isUser(request)) {
            userAuthService.log(this.getClass(), request);

            MultipartFileSender.fromPath(Paths.get(episodeDao.getById(episodeId).getPath()))
                    .with(request)
                    .with(response)
                    .serveResource();
        }
    }
}
