package ch.moviescore.core.controller;

import ch.moviescore.core.data.user.User;
import ch.moviescore.core.data.user.UserDao;
import ch.moviescore.core.service.ActivityService;
import ch.moviescore.core.service.auth.CookieService;
import ch.moviescore.core.service.auth.SessionService;
import ch.moviescore.core.service.auth.ShaService;
import ch.moviescore.core.service.auth.UserAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

/**
 * @author Wetwer
 * @project movie-db
 */

@Controller
@RequestMapping("fastlogin")
public class FastLoginController {

    private UserDao userDao;

    private UserAuthService userAuthService;
    private CookieService cookieService;
    private ActivityService activityService;
    private ShaService shaService;
    private SessionService sessionService;

    public FastLoginController(UserDao userDao, UserAuthService userAuthService, CookieService cookieService,
                               ShaService shaService, ActivityService activityService, SessionService sessionService) {

        this.userDao = userDao;
        this.userAuthService = userAuthService;
        this.cookieService = cookieService;
        this.shaService = shaService;
        this.activityService = activityService;
        this.sessionService = sessionService;
    }

    @GetMapping("{authkey}")
    public String checkFastLogin(@PathVariable("authkey") String authkey, Model model, HttpServletRequest request,
                                 HttpServletResponse response) {
        if (userAuthService.isUser(request)) {
            return "LOGEDIN";
        } else {
            userAuthService.allowGuest(model, request);
            for (User user : userDao.getAll()) {
                if (user.getAuthKey() == null) {
                    return "ERROR";
                } else if (authkey.equals(user.getAuthKey())) {
                    cookieService.setFastLoginCookie(response, user);
                    userDao.save(user);
                    activityService.log(user.getName() + " used Authkeylink", user);
                    return "FASTLOGIN";
                }
            }
            return "AUTH_ERROR";
        }
    }

    @PostMapping
    public String checkAuth(@RequestParam("authkey") String authkey, HttpServletResponse response) {

        for (User user : userDao.getAll()) {
            if (user.getAuthKey() == null) {
                return "AUTH_NULL";
            } else if (authkey.equals(user.getAuthKey())) {
                cookieService.setFastLoginCookie(response, user);
                userDao.save(user);
                activityService.log(user.getName() + " logged in with Authkey", user);
                return "AUTH_ALLOWED";
            }
        }
        return "AUTH_ERROR";
    }

    @PostMapping("settings/{userId}")
    public String saveSettings(@PathVariable("userId") Long userId, @RequestParam("name") String nameParam,
                               @RequestParam("password") String passwordParam, @RequestParam("confirm") String confirm,
                               @RequestParam(name = "player", required = false, defaultValue = "plyr") String player,
                               HttpServletRequest request, HttpServletResponse response) {
        if (cookieService.getFastLogin(request) != null && passwordParam.equals(confirm)) {
            User user = userDao.getById(userId);
            user.setName(nameParam);
            user.setPasswordSha(shaService.encode(passwordParam));
            user.setVideoPlayer(player);
            user.setAuthKey(shaService.encode(String.valueOf(new Random().nextInt())));

            LoginController.loginProcess(response, user, shaService, cookieService, sessionService);
            try {
                userDao.save(user);
            } catch (Exception e) {
                return "EXISTS";
            }
            activityService.log(user.getName() + " registered with fastlogin", user);

            userDao.save(user);
            return "LOGGEDIN";
        }
        return "AUTH_ERROR";
    }


}
