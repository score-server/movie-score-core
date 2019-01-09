package ch.moviescore.core.controller;

import ch.moviescore.core.data.groupinvite.GroupDao;
import ch.moviescore.core.data.groupinvite.GroupInvite;
import ch.moviescore.core.data.user.User;
import ch.moviescore.core.data.user.UserDao;
import ch.moviescore.core.service.ActivityService;
import ch.moviescore.core.service.auth.CookieService;
import ch.moviescore.core.service.auth.SessionService;
import ch.moviescore.core.service.auth.ShaService;
import ch.moviescore.core.service.auth.UserAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

/**
 * @author Wetwer
 * @project movie-db
 */

@Slf4j
@Controller
@RequestMapping("register")
public class RegisterController {

    private UserDao userDao;
    private GroupDao groupDao;

    private ShaService shaService;
    private UserAuthService userAuthService;
    private ActivityService activityService;
    private CookieService cookieService;
    private SessionService sessionService;

    public RegisterController(UserDao userDao, GroupDao groupDao, ShaService shaService,
                              UserAuthService userAuthService, ActivityService activityService,
                              CookieService cookieService, SessionService sessionService) {
        this.userDao = userDao;
        this.groupDao = groupDao;
        this.shaService = shaService;
        this.userAuthService = userAuthService;
        this.activityService = activityService;
        this.cookieService = cookieService;
        this.sessionService = sessionService;
    }

    @PostMapping
    public String register(@RequestParam("name") String nameParam, HttpServletRequest request) {
        User adminUser = userAuthService.getUser(request).getUser();
        if (userAuthService.isAdministrator(request)) {
            userAuthService.log(this.getClass(), request);
            if (userDao.search(nameParam).size() == 0) {
                User user = new User();
                user.setName(nameParam);
                user.setPasswordSha(shaService.encode(String.valueOf(new Random().nextInt())) + "-NOK");
                user.setRole(1);
                String authkey = shaService.encode(String.valueOf(new Random().nextInt())).substring(1, 7);
                user.setAuthKey(authkey);
                userDao.save(user);
                activityService.log(nameParam + " registered by " + adminUser.getName(), adminUser);
                return "REGISTERED";
            } else {
                return "EXISTS";
            }
        } else {
            return "AUTH_ERROR";
        }
    }

    @PostMapping("{groupKey}")
    public String registerFull(@RequestParam("name") String nameParam,
                               @RequestParam("password") String password,
                               @RequestParam("confirm") String confirm,
                               @RequestParam(name = "player", required = false, defaultValue = "") String player,
                               @PathVariable("groupKey") String groupKey,
                               HttpServletResponse response) {
        if (password.equals(confirm)) {
            if (userDao.search(nameParam).size() == 0) {
                GroupInvite group = groupDao.getByName(groupKey);
                if (group.isActive()) {
                    User user = new User();
                    user.setName(nameParam);
                    user.setPasswordSha(shaService.encode(password));
                    user.setRole(1);
                    user.setVideoPlayer(player);
                    String authkey = shaService.encode(String.valueOf(new Random().nextInt())).substring(1, 7);
                    user.setAuthKey(authkey);
                    user.setGroup(group);
                    userDao.save(user);

                    activityService.log(nameParam + " registered with groupkey " + groupKey, user);

                    LoginController.loginProcess(response, user, shaService, cookieService, sessionService, userDao, activityService);

                    return "REGISTERED";
                }
                return "NO_GROUP";
            } else {
                return "EXISTS";
            }
        }
        return "PASSWORDS_MATCH";
    }
}
