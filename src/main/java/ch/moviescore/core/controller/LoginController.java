package ch.moviescore.core.controller;

import ch.moviescore.core.data.session.SessionDao;
import ch.moviescore.core.data.user.UserDao;
import ch.moviescore.core.data.session.Session;
import ch.moviescore.core.data.user.User;
import ch.moviescore.core.service.ActivityService;
import ch.moviescore.core.service.auth.CookieService;
import ch.moviescore.core.service.auth.SessionService;
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
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

/**
 * @author Wetwer
 * @project movie-db
 */

@Slf4j
@Controller
@RequestMapping("login")
public class LoginController {

    private SessionDao sessionDao;
    private UserDao userDto;
    private CookieService cookieService;
    private ShaService shaService;
    private UserAuthService userAuthService;
    private ActivityService activityService;
    private SessionService sessionService;

    public LoginController(SessionDao sessionDao, UserDao userDto, CookieService cookieService, ShaService shaService,
                           UserAuthService userAuthService, ActivityService activityService,
                           SessionService sessionService) {
        this.sessionDao = sessionDao;
        this.userDto = userDto;
        this.cookieService = cookieService;
        this.shaService = shaService;
        this.userAuthService = userAuthService;
        this.activityService = activityService;
        this.sessionService = sessionService;
    }

    @PostMapping
    public String login(@RequestParam(name = "redirect", required = false, defaultValue = "") String redirectParam,
                        @RequestParam("name") String nameParam,
                        @RequestParam("password") String passwordParam,
                        HttpServletResponse response) {
        User user = userDto.login(nameParam, shaService.encode(passwordParam));

        try {
            userDto.exists(user);
            String sessionId = shaService.encode(String.valueOf(new Random().nextInt()));
            cookieService.setUserCookie(response, sessionId);
            sessionService.addSession(user, sessionId);
            user.setLastLogin(new Timestamp(new Date().getTime()));
            userDto.save(user);
            activityService.log(user.getName() + " logged in", user);
            switch (redirectParam) {
                case "null":
                    return "redirect:/";
                case "score":
                    return "redirect:http://scorewinner.ch";
                default:
                    return "redirect:" + redirectParam;
            }
        } catch (NullPointerException e) {
            return "redirect:/login?error&user=" + nameParam;
        }
    }

    @PostMapping("logout")
    public String logout(HttpServletRequest request) {
        if (userAuthService.isUser(request)) {
            try {
                User user = cookieService.getCurrentUser(request);
                sessionService.logout(cookieService.getSessionId(request));
                userDto.save(user);
                activityService.log(user.getName() + " logged out", user);
                return "redirect:/login?logout";
            } catch (NullPointerException e) {
                e.printStackTrace();
                return "redirect:/";
            }
        } else {
            return "redirect:/";
        }

    }

    @PostMapping("logout/{sessionId}")
    public String logout(@PathVariable("sessionId") String sessionId, HttpServletRequest request) {
        if (userAuthService.isAdministrator(request)) {
            try {
                User user = userDto.getBySessionId(sessionId);
                Session session = sessionDao.getBySessionId(sessionId);
                session.setActive(false);
                sessionDao.save(session);
                return "redirect:/user/" + user.getId();
            } catch (NullPointerException e) {
                e.printStackTrace();
                return "redirect:/";
            }
        } else {
            return "redirect:/";
        }
    }
}
