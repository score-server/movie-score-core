package ch.moviescore.core.controller;

import ch.moviescore.core.data.activitylog.ActivityLogDao;
import ch.moviescore.core.data.session.SessionDao;
import ch.moviescore.core.data.timeline.TimeLineDao;
import ch.moviescore.core.data.user.UserDao;
import ch.moviescore.core.data.session.Session;
import ch.moviescore.core.data.user.User;
import ch.moviescore.core.service.ActivityService;
import ch.moviescore.core.service.SearchService;
import ch.moviescore.core.service.auth.ShaService;
import ch.moviescore.core.service.auth.UserAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

/**
 * @author Wetwer
 * @project movie-db
 */

@Slf4j
@Controller
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
    public String getUserList(@RequestParam(name = "search", required = false, defaultValue = "") String search,
                              Model model, HttpServletRequest request) {
        if (userAuthService.isUser(model, request)) {
            userAuthService.log(this.getClass(), request);
            model.addAttribute("users", searchService.searchUser(search));
            model.addAttribute("search", search);
            model.addAttribute("page", "userList");
            return "template";
        } else {
            return "redirect:/login?redirect=/user";
        }
    }

    @GetMapping(value = "{userId}")
    public String getOneUser(@PathVariable("userId") Long userId, Model model, HttpServletRequest request) {
        if (userAuthService.isUser(model, request)) {
            userAuthService.log(this.getClass(), request);
            User user = userDao.getById(userId);

            model.addAttribute("user", user);
            model.addAttribute("sessions", sessionDao.getByUser(user));
            model.addAttribute("currentSession", userAuthService.getCurrentSessionId(request));
            model.addAttribute("requests", user.getRequests());
            model.addAttribute("timelines", timeLineDao.getByUser(user));
            model.addAttribute("activities",
                    logDao.getAllByUser(user));
            if (user.getPasswordSha().endsWith("-NOK")) {
                model.addAttribute("registered", false);
            } else {
                model.addAttribute("registered", true);
            }


            model.addAttribute("page", "user");
            return "template";
        } else {
            return "redirect:/login?redirect=/user/" + userId;
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
            return "redirect:/user/" + userId + "?role";
        } else {
            return "redirect:/user/" + userId + "?error";
        }
    }

    @PostMapping(value = "{userId}/group/{alpha}")
    public String setAlpha(@PathVariable("userId") Long userId, @PathVariable("alpha") String alpha,
                           HttpServletRequest request) {
        User currentUser = userAuthService.getUser(request).getUser();

        if (userAuthService.isAdministrator(request)) {
            userAuthService.log(this.getClass(), request);
            User user = userDao.getById(userId);
            if (alpha.equals("1")) {
                activityService.log(currentUser.getName() + " is Sexy", user);
                user.setSexabig(true);
            } else if (alpha.equals("0")) {
                user.setSexabig(false);
                activityService.log(currentUser.getName() + " is no longer Sexy", user);
            }
            userDao.save(user);
            return "redirect:/user/" + userId;
        } else {
            return "redirect:/user/" + userId + "?error";
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
            return "redirect:/user/" + userId + "?username";
        } else {
            return "redirect:/user/" + userId + "?error";
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
            return "redirect:/user/" + userId + "?password";
        } else {
            return "redirect:/user/" + userId + "?error";
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
            switch (videoPlayer) {
                case "plyr":
                    return "redirect:/user/" + userId + "?player=Plyr.io";
                case "html5":
                    return "redirect:/user/" + userId + "?player=HTML 5 Player";
                default:
                    return "redirect:/user/" + userId;
            }
        } else {
            return "redirect:/user/" + userId + "?error";
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
        }
        return "redirect:/user/" + userId;
    }
}
