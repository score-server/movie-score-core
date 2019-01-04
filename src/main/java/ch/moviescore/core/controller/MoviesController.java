package ch.moviescore.core.controller;

import ch.moviescore.core.model.api.MovieListModel;
import ch.moviescore.core.service.PageService;
import ch.moviescore.core.service.SearchService;
import ch.moviescore.core.service.auth.UserAuthService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("movies")
public class MoviesController {

    private SearchService searchService;
    private UserAuthService userAuthService;
    private PageService pageService;

    public MoviesController(SearchService searchService, UserAuthService userAuthService, PageService pageService) {
        this.searchService = searchService;
        this.userAuthService = userAuthService;
        this.pageService = pageService;
    }

    @GetMapping(value = "{page}", produces = "application/json")
    public @ResponseBody
    MovieListModel getMovies(@PathVariable(name = "page", required = false) Integer page,
                             @RequestParam(name = "search", required = false, defaultValue = "") String search,
                             @RequestParam(name = "orderBy", required = false, defaultValue = "") String orderBy,
                             @RequestParam(name = "genre", required = false, defaultValue = "") String genreParam,
                             Model model, HttpServletRequest request) {
        if (userAuthService.isUser(model, request)) {
            try {
                userAuthService.log(this.getClass(), request);

                MovieListModel movieListModel = new MovieListModel();
                movieListModel.setSearchParam(search);
                movieListModel.setOrderBy(orderBy);
                movieListModel.setGenre(genreParam);
                movieListModel.setPage(page);
                movieListModel.setMovies(pageService.getPage(
                        searchService.searchMovies(search, orderBy, genreParam), page));

                return movieListModel;
            } catch (NullPointerException e) {
                return null;
            }
        } else {
            return null;
        }
    }

}
