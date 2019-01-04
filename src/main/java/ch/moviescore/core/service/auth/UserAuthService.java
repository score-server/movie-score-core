package ch.moviescore.core.service.auth;

import ch.moviescore.core.data.user.User;
import ch.moviescore.core.data.user.UserDao;
import ch.moviescore.core.service.filehandler.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Wetwer
 * @project movie-db
 */
@Service
public class UserAuthService {

    private UserDao userDao;

    private CookieService cookieService;
    private SettingsService settingsService;

    public UserAuthService(UserDao userDao, CookieService cookieService, SettingsService settingsService) {
        this.userDao = userDao;
        this.cookieService = cookieService;
        this.settingsService = settingsService;
    }

    public UserIndicator getUser(HttpServletRequest request) {
        UserIndicator userIndicator = new UserIndicator();
        try {
            userIndicator.setUser(cookieService.getCurrentUser(request));
            userIndicator.setLoggedIn(true);
        } catch (NullPointerException ignored) {
            userIndicator.setLoggedIn(false);
        }
        return userIndicator;
    }

    public void allowGuest(Model model, HttpServletRequest request) {
        UserIndicator userIndicator = getUser(request);
        model.addAttribute("restart", settingsService.getKey("restart"));

        if (userIndicator.isLoggedIn()) {
            model.addAttribute("currentUser", userIndicator.getUser());
        }
    }

    public boolean isUser(Model model, HttpServletRequest request) {
        UserIndicator userIndicator = getUser(request);

        if (userIndicator.isLoggedIn()) {
            model.addAttribute("restart", settingsService.getKey("restart"));
            model.addAttribute("currentUser", userIndicator.getUser());
            return true;
        }

        return false;
    }

    public boolean isUser(HttpServletRequest request) {
        UserIndicator userIndicator = getUser(request);

        return userIndicator.isLoggedIn();
    }

    public boolean isUser(String sessionId) {
        User user = userDao.getBySessionId(sessionId);

        return user != null;
    }


    public boolean isAdministrator(Model model, HttpServletRequest request) {
        UserIndicator userIndicator = getUser(request);

        if (userIndicator.isLoggedIn()) {
            model.addAttribute("restart", settingsService.getKey("restart"));
            model.addAttribute("currentUser", userIndicator.getUser());
            return userIndicator.getUser().getRole() == 2;
        }
        return false;
    }

    public boolean isAdministrator(HttpServletRequest request) {
        UserIndicator userIndicator = getUser(request);

        if (userIndicator.isLoggedIn()) {
            return userIndicator.getUser().getRole() == 2;
        }
        return false;
    }

    public boolean isCurrentUser(Model model, HttpServletRequest request, User user) {
        UserIndicator userIndicator = getUser(request);

        if (userIndicator.isLoggedIn()) {
            model.addAttribute("restart", settingsService.getKey("restart"));
            model.addAttribute("currentUser", userIndicator.getUser());
            return userIndicator.getUser() == user;
        }
        return false;
    }

    public boolean isCurrentUser(HttpServletRequest request, User user) {
        UserIndicator userIndicator = getUser(request);

        if (userIndicator.isLoggedIn()) {
            return userIndicator.getUser() == user;
        }
        return false;
    }

    public void log(Object controller, HttpServletRequest request) {
        log(controller, getUser(request).getUser());
    }

    public void log(Object controller, User user) {
        final Logger LOG = LoggerFactory.getLogger(controller.toString());
        LOG.info(user.getName());
    }

    public String getCurrentSessionId(HttpServletRequest request) {
        return cookieService.getSessionId(request);
    }
}
