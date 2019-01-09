package ch.moviescore.core.controller;

import ch.moviescore.core.data.activitylog.ActivityLogDao;
import ch.moviescore.core.data.session.Session;
import ch.moviescore.core.data.session.SessionDao;
import ch.moviescore.core.data.timeline.TimeLineDao;
import ch.moviescore.core.data.user.User;
import ch.moviescore.core.data.user.UserDao;
import ch.moviescore.core.model.api.UserListModel;
import ch.moviescore.core.service.ActivityService;
import ch.moviescore.core.service.SearchService;
import ch.moviescore.core.service.auth.ShaService;
import ch.moviescore.core.service.auth.UserAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

/**
 * @author Wetwer
 * @project movie-db
 */

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    private TimeLineDao timeLineDao;
    private ActivityLogDao logDao;
    private UserDao userDao;
    private SessionDao sessionDao;

    private ShaService shaService;
    private SearchService searchService;
    private UserAuthService userAuthService;
    private ActivityService activityService;


    public UserController(TimeLineDao timeLineDao, UserDao userDao, ShaService shaService, SearchService searchService,
                          UserAuthService userAuthService, ActivityService activityService, ActivityLogDao logDao,
                          SessionDao sessionDao) {
        this.timeLineDao = timeLineDao;
        this.userDao = userDao;
        this.shaService = shaService;
        this.searchService = searchService;
        this.userAuthService = userAuthService;
        this.activityService = activityService;
        this.logDao = logDao;
        this.sessionDao = sessionDao;
    }

    @GetMapping
    public UserListModel getUserList(@RequestParam(name = "search", required = false, defaultValue = "") String search,
                                     Model model, HttpServletRequest request) {
        if (userAuthService.isUser(model, request)) {
            userAuthService.log(this.getClass(), request);

            UserListModel userListModel = new UserListModel();
            userListModel.setUserList(searchService.searchUser(search));
            userListModel.setSearch(search);

            return userListModel;
        } else {
            return null;
        }
    }

    @GetMapping(value = "{userId}")
    public User getOneUser(@PathVariable("userId") Long userId, HttpServletRequest request) {
        if (userAuthService.isUser(request)) {
            userAuthService.log(this.getClass(), request);

            return userDao.getById(userId);
        } else {
            return null;
        }
    }

    @GetMapping("current")
    public User getCurrentUser(HttpServletRequest request) {
        if (userAuthService.isUser(request)) {
            return userAuthService.getUser(request).getUser();
        } else {
            return null;
        }
    }


    @PostMapping(value = "{userId}/role/{role}")
    public String setRole(@PathVariable("userId") Long userId, @PathVariable("role") String role,
                          HttpServletRequest request) {
        User currentUser = userAuthService.getUser(request).getUser();

        if (userAuthService.isAdministrator(request)) {
            userAuthService.log(this.getClass(), request);
            User user = userDao.getById(userId);
            user.setRole(Integer.valueOf(role));
            userDao.save(user);
            activityService.log(currentUser.getName() + " changed role of " + user.getName() + " to " + role, user);
            return "CHANGED";
        } else {
            return "AUTH_ERROR";
        }
    }

    @PostMapping("{userId}/name")
    public String setUsername(@PathVariable("userId") Long userId, @RequestParam("name") String newName,
                              Model model, HttpServletRequest request) {
        User user = userDao.getById(userId);
        String oldName = user.getName();
        if (userAuthService.isCurrentUser(model, request, user)) {
            userAuthService.log(this.getClass(), request);
            user.setName(newName);
            userDao.save(user);
            activityService.log(oldName + " changed Username to " + newName, user);
            return "CHANGED";
        } else {
            return "AUTH_ERROR";
        }
    }

    @PostMapping("{userId}/password")
    public String setPassword(@PathVariable("userId") Long userId,
                              @RequestParam("old") String oldPassword,
                              @RequestParam("new") String newPassword, Model model, HttpServletRequest request) {

        User user = userDao.getByIdAndPasswordSha(userId, shaService.encode(oldPassword));

        if (userAuthService.isCurrentUser(model, request, user)) {
            userAuthService.log(this.getClass(), request);
            user.setPasswordSha(shaService.encode(newPassword));
            userDao.save(user);
            for (Session session : user.getSessions()) {
                sessionDao.deactivate(session);
            }
            activityService.log(user.getName() + " changed Password", user);
            return "CHANGED";
        } else {
            return "AUTH_ERROR";
        }
    }

    @PostMapping("{userId}/player")
    public String setPlayer(@PathVariable("userId") Long userId,
                            @RequestParam("player") String videoPlayer,
                            Model model, HttpServletRequest request) {

        User user = userDao.getById(userId);

        if (userAuthService.isCurrentUser(model, request, user)) {
            userAuthService.log(this.getClass(), request);
            user.setVideoPlayer(videoPlayer);
            userDao.save(user);
            activityService.log(user.getName() + " set Video Player to " + videoPlayer, user);
            return "CHANGED";
        } else {
            return "AUTH_ERROR";
        }
    }

    @PostMapping("generate/{userId}")
    public String generateKey(@PathVariable("userId") Long userId,
                              @RequestParam(name = "extended", required = false, defaultValue = "") String extendedAuth,
                              HttpServletRequest request) {
        if (userAuthService.isAdministrator(request)) {
            userAuthService.log(this.getClass(), request);
            User user = userDao.getById(userId);
            if (extendedAuth.equals("extended")) {
                user.setAuthKey(shaService.encode(String.valueOf(new Random().nextInt())));
            } else {
                user.setAuthKey(shaService.encode(String.valueOf(new Random().nextInt())).substring(1, 7));
            }
            userDao.save(user);
            return "GENERATED";
        }
        return "AUTH_ERROR";
    }
}
