package ch.moviescore.core.controller;

import ch.moviescore.core.data.user.User;
import ch.moviescore.core.service.ActivityService;
import ch.moviescore.core.service.auth.UserAuthService;
import ch.moviescore.core.service.filehandler.SettingsService;
import ch.moviescore.core.service.importer.MovieImportService;
import ch.moviescore.core.service.importer.SeriesImportService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Wetwer
 * @project movie-db
 */
@RestController
@RequestMapping("import")
public class ImportController {

    private UserAuthService userAuthService;
    private MovieImportService newMovieMovieImportService;
    private SeriesImportService newSeriesImportService;
    private ActivityService activityService;
    private SettingsService settingsService;

    public ImportController(UserAuthService userAuthService, MovieImportService newMovieMovieImportService,
                            SeriesImportService newSeriesImportService, ActivityService activityService,
                            SettingsService settingsService) {
        this.userAuthService = userAuthService;
        this.newMovieMovieImportService = newMovieMovieImportService;
        this.newSeriesImportService = newSeriesImportService;
        this.activityService = activityService;
        this.settingsService = settingsService;
    }

    @PostMapping(value = "movie")
    public String importMovies(Model model, HttpServletRequest request) {
        User user = userAuthService.getUser(request).getUser();
        if (userAuthService.isAdministrator(model, request)) {
            if (settingsService.getKey("import").equals("1")) {
                return "redirect:/settings";
            }
            newMovieMovieImportService.importAll();
            activityService.log(user.getName() + " started Movie Import", user);
            return "IMPORTING";
        } else {
            return "AUTH_ERROR";
        }
    }

    @PostMapping(value = "movie/update")
    public String updateMovies(Model model, HttpServletRequest request) {
        User user = userAuthService.getUser(request).getUser();
        if (userAuthService.isAdministrator(model, request)) {
            if (settingsService.getKey("import").equals("1")) {
                return "redirect:/settings";
            }
            newMovieMovieImportService.updateAll();
            activityService.log(user.getName() + " started Movie Update", user);
            return "UPDATING";
        } else {
            return "AUTH_ERROR";
        }
    }

    @PostMapping(value = "serie")
    public String importSeries(Model model, HttpServletRequest request) {
        User user = userAuthService.getUser(request).getUser();
        if (userAuthService.isAdministrator(model, request)) {
            if (settingsService.getKey("import").equals("1")) {
                return "redirect:/settings";
            }
            newSeriesImportService.importAll();
            activityService.log(user.getName() + " started Series Import", user);
            return "IMPORTING";
        } else {
            return "AUTH_ERROR";
        }
    }

    @PostMapping(value = "serie/update")
    public String updateSeries(Model model, HttpServletRequest request) {
        User user = userAuthService.getUser(request).getUser();
        if (userAuthService.isAdministrator(model, request)) {
            if (settingsService.getKey("import").equals("1")) {
                return "redirect:/settings";
            }
            newSeriesImportService.updateAll();
            activityService.log(user.getName() + " started Series Update", user);
            return "UPDATING";
        } else {
            return "AUTH_ERROR";
        }
    }


    @PostMapping("path/movie")
    public String setMovieImportPath(@RequestParam("path") String pathParam, Model model, HttpServletRequest request) {
        if (userAuthService.isAdministrator(model, request)) {
            settingsService.setValue("moviePath", pathParam);
            return "SAVED";
        } else {
            return "AUTH_ERROR";
        }
    }

    @PostMapping("path/serie")
    public String setSerieImportPath(@RequestParam("path") String pathParam, Model model, HttpServletRequest request) {
        if (userAuthService.isAdministrator(model, request)) {
            settingsService.setValue("seriePath", pathParam);
            return "SAVED";
        } else {
            return "AUTH_ERROR";
        }
    }

    @PostMapping("reset")
    public String importReset(Model model, HttpServletRequest request) {
        if (userAuthService.isAdministrator(model, request)) {
            newMovieMovieImportService.setImportStatus("0");
            return "CLEARED";
        } else {
            return "AUTH_ERROR";
        }
    }
}
