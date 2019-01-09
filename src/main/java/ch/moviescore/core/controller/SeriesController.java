package ch.moviescore.core.controller;

import ch.moviescore.core.data.serie.Serie;
import ch.moviescore.core.data.serie.SerieDao;
import ch.moviescore.core.model.api.SerieListModel;
import ch.moviescore.core.service.SearchService;
import ch.moviescore.core.service.auth.UserAuthService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Wetwer
 * @project movie-db
 */
@RestController
@RequestMapping("serie")
public class SeriesController {

    private SerieDao serieDao;

    private SearchService searchService;
    private UserAuthService userAuthService;

    public SeriesController(SerieDao serieDao, SearchService searchService, UserAuthService userAuthService) {
        this.serieDao = serieDao;
        this.searchService = searchService;
        this.userAuthService = userAuthService;
    }

    @GetMapping
    public SerieListModel getSeries(@RequestParam(name = "search", required = false, defaultValue = "") String search,
                                    @RequestParam(name = "genre", required = false, defaultValue = "") String genreParam,
                                    Model model, HttpServletRequest request) {
        if (userAuthService.isUser(model, request)) {
            userAuthService.log(this.getClass(), request);

            SerieListModel serieListModel = new SerieListModel();
            serieListModel.setGenre(genreParam);
            serieListModel.setSearchParam(search);
            serieListModel.setSeries(searchService.searchSerie(search, genreParam));

            return serieListModel;
        } else {
            return null;
        }
    }

    @GetMapping(value = "/{serieId}")
    public Serie getOneSerie(@PathVariable("serieId") Long serieId, Model model, HttpServletRequest request) {
        if (userAuthService.isUser(model, request)) {
            return serieDao.getById(serieId);
        } else {
            return null;
        }

    }
}
