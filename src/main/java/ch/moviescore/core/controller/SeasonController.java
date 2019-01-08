package ch.moviescore.core.controller;

import ch.moviescore.core.data.episode.EpisodeDao;
import ch.moviescore.core.data.season.Season;
import ch.moviescore.core.data.season.SeasonDao;
import ch.moviescore.core.service.auth.UserAuthService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Wetwer
 * @project movie-db
 */
@RestController
@RequestMapping("season")
public class SeasonController {

    private SeasonDao seasonDao;
    private EpisodeDao episodeDao;

    private UserAuthService userAuthService;

    public SeasonController(SeasonDao seasonDao, EpisodeDao episodeDao, UserAuthService userAuthService) {
        this.seasonDao = seasonDao;
        this.userAuthService = userAuthService;
        this.episodeDao = episodeDao;
    }

    @GetMapping(value = "/{seasonId}", produces = "application/json")
    public Season getOneSeason(@PathVariable("seasonId") Long seasonId, Model model, HttpServletRequest request) {
        if (userAuthService.isUser(model, request)) {
            userAuthService.log(this.getClass(), request);

            return seasonDao.getById(seasonId);
        } else {
            return null;
        }
    }

}
