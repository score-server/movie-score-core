package ch.moviescore.core.controller;

import ch.moviescore.core.data.episode.EpisodeDao;
import ch.moviescore.core.data.movie.MovieDao;
import ch.moviescore.core.data.serie.SerieDao;
import ch.moviescore.core.data.updatelog.UpdateLogDao;
import ch.moviescore.core.model.PageInfoModel;
import ch.moviescore.core.service.auth.UserAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Wetwer
 * @project movie-db
 */

@Controller
@RequestMapping("info")
public class InfoController {

    private EpisodeDao episodeDao;
    private MovieDao movieDao;
    private SerieDao serieDao;
    private UpdateLogDao updateLogDao;

    private UserAuthService userAuthService;

    public InfoController(EpisodeDao episodeDao, MovieDao movieDao, SerieDao serieDao, UpdateLogDao updateLogDao,
                          UserAuthService userAuthService) {
        this.episodeDao = episodeDao;
        this.movieDao = movieDao;
        this.updateLogDao = updateLogDao;
        this.serieDao = serieDao;
        this.userAuthService = userAuthService;
    }

    @GetMapping
    public PageInfoModel getPageinfo(Model model, HttpServletRequest request) {
        if (userAuthService.isUser(model, request)) {
            userAuthService.log(this.getClass(), request);

            PageInfoModel pageInfoModel = new PageInfoModel();
            pageInfoModel.setAmmountMovies(movieDao.getAll().size());
            pageInfoModel.setAmmountSeries(serieDao.getAll().size());
            pageInfoModel.setAmmountEpisodes(episodeDao.getAll().size());
            pageInfoModel.setLatestMovies(movieDao.getLatestInfo());
            pageInfoModel.setUpdateLogs(updateLogDao.getAll());

            return pageInfoModel;
        } else {
            return null;
        }
    }
}
