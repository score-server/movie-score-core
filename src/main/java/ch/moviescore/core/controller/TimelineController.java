package ch.moviescore.core.controller;

import ch.moviescore.core.data.listmovie.ListMovie;
import ch.moviescore.core.data.listmovie.ListMovieDao;
import ch.moviescore.core.data.movie.MovieDao;
import ch.moviescore.core.data.timeline.TimeLineDao;
import ch.moviescore.core.data.timeline.Timeline;
import ch.moviescore.core.data.user.User;
import ch.moviescore.core.service.ActivityService;
import ch.moviescore.core.service.ListService;
import ch.moviescore.core.service.auth.UserAuthService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("timeline")
public class TimelineController {

    private MovieDao movieDao;
    private TimeLineDao timeLineDao;
    private ListMovieDao listMovieDao;

    private UserAuthService userAuthService;
    private ActivityService activityService;
    private ListService listService;

    public TimelineController(MovieDao movieDao, TimeLineDao timeLineDao, ListMovieDao listMovieDao,
                              UserAuthService userAuthService, ActivityService activityService,
                              ListService listService) {
        this.movieDao = movieDao;
        this.timeLineDao = timeLineDao;
        this.listMovieDao = listMovieDao;
        this.userAuthService = userAuthService;
        this.activityService = activityService;
        this.listService = listService;
    }

    @PostMapping("edit/{timelineId}")
    public String saveNewMovie(@PathVariable("timelineId") Long timeLineId,
                               @RequestParam("place") String place,
                               @RequestParam("movie") Long movieId,
                               HttpServletRequest request) {
        if (userAuthService.isUser(request)) {
            userAuthService.log(this.getClass(), request);
            Timeline timeline = timeLineDao.getById(timeLineId);
            if (userAuthService.isCurrentUser(request, timeline.getUser())
                    || userAuthService.isAdministrator(request)) {
                ListMovie listMovie = new ListMovie();
                listMovie.setPlace(Integer.valueOf(place));
                listMovie.setMovie(movieDao.getById(movieId));
                listMovie.setTimeline(timeline);
                listMovieDao.save(listMovie);
                return "ADDED";
            }
            return "AUTH_ERROR";
        }
        return "AUTH_ERROR";
    }


    @PostMapping("attributes/{timelineId}")
    public String editListAttributes(@PathVariable("timelineId") Long timeLineId,
                                     @RequestParam("title") String title,
                                     @RequestParam("description") String description,
                                     HttpServletRequest request) {
        if (userAuthService.isUser(request)) {
            userAuthService.log(this.getClass(), request);

            Timeline timeline = timeLineDao.getById(timeLineId);

            if (userAuthService.isCurrentUser(request, timeline.getUser())
                    || userAuthService.isAdministrator(request)) {
                timeline.setTitle(title);
                timeline.setDescription(description);
                timeLineDao.save(timeline);

                return "CHANGED";
            }
            return "AUTH_ERROR";
        }
        return "AUTH_ERROR";
    }

    @PostMapping("delete/movie/{movieId}")
    public String deleteFromList(@PathVariable("movieId") Long movieId, HttpServletRequest request) {
        if (userAuthService.isUser(request)) {
            userAuthService.log(this.getClass(), request);

            ListMovie listMovie = listMovieDao.getById(movieId);

            if (userAuthService.isCurrentUser(request, listMovie.getTimeline().getUser())
                    || userAuthService.isAdministrator(request)) {
                listMovieDao.delete(listMovie);

                return "REMOVED";
            }
        }
        return "AUTH_ERROR";
    }

    @PostMapping("new")
    public String createList(@RequestParam("title") String title,
                             @RequestParam("description") String description,
                             HttpServletRequest request) {
        if (userAuthService.isUser(request)) {
            userAuthService.log(this.getClass(), request);
            User user = userAuthService.getUser(request).getUser();

            Timeline timeline = new Timeline();
            timeline.setTitle(title);
            timeline.setUser(user);
            timeline.setDescription(description);
            timeLineDao.save(timeline);

            activityService.log(user.getName() + " created list " + title, user);
            return "CREATED";
        } else {
            return "AUTH_ERROR";
        }
    }

    @PostMapping("delete/{timelineId}")
    public String deleteTimeline(@PathVariable("timelineId") Long timeLineId,
                                 Model model, HttpServletRequest request) {
        if (userAuthService.isUser(model, request)) {
            userAuthService.log(this.getClass(), request);

            Timeline timeline = timeLineDao.getById(timeLineId);

            if (userAuthService.isCurrentUser(request, timeline.getUser())
                    || userAuthService.isAdministrator(request)) {
                timeLineDao.delete(timeline);

                return "DELETED";
            }
        }
        return "AUTH_ERROR";
    }
}
