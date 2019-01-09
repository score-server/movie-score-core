package ch.moviescore.core.controller;

import ch.moviescore.core.data.movie.MovieDao;
import ch.moviescore.core.data.request.Request;
import ch.moviescore.core.data.request.RequestDao;
import ch.moviescore.core.data.user.User;
import ch.moviescore.core.data.user.UserDao;
import ch.moviescore.core.service.ActivityService;
import ch.moviescore.core.service.auth.UserAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Wetwer
 * @project movie-db
 */

@Slf4j
@Controller
@RequestMapping("request")
public class RequestController {

    private RequestDao requestDao;
    private UserDao userDao;
    private MovieDao movieDao;

    private UserAuthService userAuthService;
    private ActivityService activityService;

    public RequestController(RequestDao requestDao, UserDao userDao, MovieDao movieDao, UserAuthService userAuthService,
                             ActivityService activityService) {
        this.requestDao = requestDao;
        this.userDao = userDao;
        this.movieDao = movieDao;
        this.userAuthService = userAuthService;
        this.activityService = activityService;
    }

    @GetMapping(produces = "application/json")
    public List<Request> getRequests(HttpServletRequest request) {
        if (userAuthService.isUser(request)) {
            return requestDao.getAll();
        } else {
            return null;
        }
    }

    @PostMapping("create/{userId}")
    public String createRequest(@PathVariable("userId") Long userId, @RequestParam("request") String requestParam,
                                @RequestParam("type") String type, HttpServletRequest request) {
        if (userAuthService.isUser(request)) {
            User user = userDao.getById(userId);

            Request movieRequest = new Request();
            if ("user".equals(type)) {
                movieRequest.setRequest("User Request: " + requestParam);
            } else {
                movieRequest.setRequest(requestParam);
            }

            movieRequest.setUser(user);
            movieRequest.setActive("1");
            requestDao.save(movieRequest);
            activityService.log(user.getName() + " created Request for " + requestParam, user);
            return "CREATED";
        } else {
            return "AUTH_ERROR";
        }
    }

    @PostMapping("create/takedown")
    public String createTakedownRequest(@RequestParam("movie") Long movieId,
                                        @RequestParam("email") String email, HttpServletRequest request) {
        if (userAuthService.isUser(request)) {
            User user = userAuthService.getUser(request).getUser();
            Request movieRequest = new Request();
            movieRequest.setRequest("Takedown Request: "
                    + movieDao.getById(movieId).getTitle() + " " + email);
            movieRequest.setUser(user);
            movieRequest.setActive("1");
            requestDao.save(movieRequest);
            activityService.log(user.getName() + " created Takedown Request for Movie " + movieId, user);
            return "CREATED";
        } else {
            return "AUTH_ERROR";
        }
    }

    @PostMapping("{requestId}/close")
    public String closeRequest(@PathVariable("requestId") Long requestId, HttpServletRequest request) {
        if (userAuthService.isAdministrator(request)) {
            Request movieRequest = requestDao.getById(requestId);
            movieRequest.setActive("0");
            requestDao.save(movieRequest);
            return "CLOSED";
        } else {
            return "AUTH_ERROR";
        }
    }

    @PostMapping("{requestId}/open")
    public String openRequest(@PathVariable("requestId") Long requestId, HttpServletRequest request) {
        if (userAuthService.isAdministrator(request)) {
            Request movieRequest = requestDao.getById(requestId);
            movieRequest.setActive("1");
            requestDao.save(movieRequest);
            return "OPENED";
        } else {
            return "AUTH_ERROR";
        }
    }

    @PostMapping("{requestId}/delete")
    public String deleteRequest(@PathVariable("requestId") Long requestId, HttpServletRequest request) {
        Request movieRequest = requestDao.getById(requestId);
        if (userAuthService.isAdministrator(request)
                || userAuthService.isCurrentUser(request, movieRequest.getUser())) {
            requestDao.delete(movieRequest);
            return "DELETED";
        } else {
            return "AUTH_ERROR";
        }
    }

    @PostMapping("{requestId}/edit")
    public String editRequest(@PathVariable("requestId") Long requestId,
                              @RequestParam("request") String newRequest,
                              HttpServletRequest request) {

        Request movieRequest = requestDao.getById(requestId);
        if (userAuthService.isAdministrator(request)
                || userAuthService.isCurrentUser(request, movieRequest.getUser())) {

            movieRequest.setRequest(newRequest);
            requestDao.save(movieRequest);
            userAuthService.log(this.getClass(), movieRequest.getUser());

            return "CHANGED";
        } else {
            return "AUTH_ERROR";
        }
    }

}
