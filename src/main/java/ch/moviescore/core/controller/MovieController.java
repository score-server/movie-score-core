package ch.moviescore.core.controller;

import ch.moviescore.core.data.likes.Likes;
import ch.moviescore.core.data.likes.LikesDao;
import ch.moviescore.core.data.movie.Movie;
import ch.moviescore.core.data.movie.MovieDao;
import ch.moviescore.core.data.time.TimeDao;
import ch.moviescore.core.data.user.User;
import ch.moviescore.core.model.api.MovieModel;
import ch.moviescore.core.service.ActivityService;
import ch.moviescore.core.service.SimilarMovieService;
import ch.moviescore.core.service.auth.UserAuthService;
import ch.moviescore.core.service.importer.MovieImportService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * @author Wetwer
 * @project movie-db
 */

@RestController
@RequestMapping("movie")
public class MovieController {

    private MovieDao movieDto;
    private LikesDao likesDto;
    private TimeDao timeDto;

    private SimilarMovieService similarMovieService;
    private UserAuthService userAuthService;
    private ActivityService activityService;
    private MovieImportService movieImportService;

    public MovieController(MovieDao movieDto, LikesDao likesDto, SimilarMovieService similarMovieService,
                           UserAuthService userAuthService, ActivityService activityService, TimeDao timeDto,
                           MovieImportService movieImportService) {
        this.movieDto = movieDto;
        this.likesDto = likesDto;
        this.timeDto = timeDto;
        this.similarMovieService = similarMovieService;
        this.userAuthService = userAuthService;
        this.activityService = activityService;
        this.movieImportService = movieImportService;
    }


    @GetMapping(value = "{movieId}", produces = "application/json")
    public @ResponseBody
    MovieModel getOneMovie(@PathVariable("movieId") Long movieId, Model model, HttpServletRequest request) {
        if (userAuthService.isUser(model, request)) {
            userAuthService.log(this.getClass(), request);

            Movie movie = movieDto.getById(movieId);
            User user = userAuthService.getUser(request).getUser();

            MovieModel movieModel = new MovieModel();
            movieModel.setMovie(movie);
            movieModel.setSimilarMovies(similarMovieService.getSimilarMovies(movie));
            setTimeAndLikes(request, movie, user, movieModel);

            return movieModel;
        } else {
            return null;
        }
    }

    @PostMapping("{movieId}/like")
    public String likeMovie(@PathVariable("movieId") Long movieId, HttpServletRequest request) {
        if (userAuthService.isUser(request)) {
            User user = userAuthService.getUser(request).getUser();
            Movie movie = movieDto.getById(movieId);
            try {
                Likes likes = likesDto.getByUserAndMovie(user, movie);
                likes.getId();
                likesDto.delete(likes);
                activityService.log(user.getName() + " removed like on movie " + movie.getTitle(), user);
                return "REMOVED_LIKED";
            } catch (NullPointerException e) {
                Likes likes = new Likes();
                likes.setMovie(movie);
                likes.setUser(user);
                likesDto.save(likes);
                activityService.log(user.getName() + " likes movie " + movie.getTitle(), user);
                return "LIKED";
            }
        } else {
            return "AUTH_ERROR";
        }
    }


    @PostMapping("{movieId}/path")
    public String setMoviePath(@PathVariable("movieId") Long movieId, @RequestParam("path") String path,
                               HttpServletRequest request) {
        if (userAuthService.isAdministrator(request)) {
            User user = userAuthService.getUser(request).getUser();
            Movie movie = movieDto.getById(movieId);
            movie.setVideoPath(path);
            movieDto.save(movie);
            movieImportService.updateFile(new File(path));
            activityService.log(user.getName() + " changed Path on Movie " + movie.getTitle() + " to " + path, user);
            return "CHANGED";
        } else {
            return "AUTH_ERROR";
        }
    }

    @PostMapping("{movieId}/attributes")
    public String setMovieAttributes(@PathVariable("movieId") Long movieId, @RequestParam("quality") String quality,
                               @RequestParam("year") String year,
                               @RequestParam("tmdbId") Integer tmdbId,
                               HttpServletRequest request) {
        if (userAuthService.isAdministrator(request)) {
            Movie movie = movieDto.getById(movieId);
            movie.setQuality(quality);
            movie.setYear(year);
            movie.setTmdbId(tmdbId);
            movieDto.save(movie);
            movieImportService.updateFile(new File(movie.getVideoPath()));
            return "CHANGED";
        } else {
            return "AUTH_ERROR";
        }
    }

    private void setTimeAndLikes(HttpServletRequest request, Movie movie, User user, MovieModel movieModel) {
        try {
            movieModel.setTime(timeDto.getByUserAndMovie(user, movie).getTime());
        } catch (NullPointerException e) {
            movieModel.setTime(0f);
        }

        try {
            Likes likes = likesDto.getByUserAndMovie(userAuthService.getUser(request).getUser(), movie);
            likes.getId();
            movieModel.setLiked(true);
        } catch (NullPointerException e) {
            movieModel.setLiked(false);
        }
    }
}
