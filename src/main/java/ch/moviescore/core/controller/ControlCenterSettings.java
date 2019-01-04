package ch.moviescore.core.controller;


import ch.moviescore.core.data.activitylog.ActivityLogDao;
import ch.moviescore.core.data.importlog.ImportLogDao;
import ch.moviescore.core.data.request.RequestDao;
import ch.moviescore.core.service.filehandler.SettingsService;
import ch.moviescore.core.service.auth.UserAuthService;
import ch.moviescore.core.service.filehandler.FileHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Wetwer
 * @project movie-db
 */

@Controller
@RequestMapping("settings")
public class ControlCenterSettings {

    private ImportLogDao importLogDao;
    private ActivityLogDao activityLogDao;
    private RequestDao requestDao;

    private SettingsService settingsService;
    private UserAuthService userAuthService;

    public ControlCenterSettings(SettingsService settingsService, UserAuthService userAuthService,
                                 ImportLogDao importLogDao, ActivityLogDao activityLogDao, RequestDao requestDao) {
        this.settingsService = settingsService;
        this.userAuthService = userAuthService;
        this.importLogDao = importLogDao;
        this.activityLogDao = activityLogDao;
        this.requestDao = requestDao;
    }

    @GetMapping
    private String getControlCenter(Model model, HttpServletRequest request) {
        if (userAuthService.isAdministrator(model, request)) {

            model.addAttribute("moviePath", settingsService.getKey("moviePath"));
            model.addAttribute("seriePath", settingsService.getKey("seriePath"));
            model.addAttribute("importProgress", settingsService.getKey("importProgress"));
            model.addAttribute("importLogs", importLogDao.getAll());
            model.addAttribute("activityLogs", activityLogDao.getAll());
            model.addAttribute("requests", requestDao.getAll());
            try {
                model.addAttribute("running", settingsService.getKey("import").equals("1"));
            } catch (NullPointerException e) {
                settingsService.setValue("import", "0");
                model.addAttribute("running", settingsService.getKey("import").equals("1"));
            }

            try {
                model.addAttribute("restart", settingsService.getKey("restart"));
            } catch (NullPointerException e) {

            }

            model.addAttribute("page", "controlCenter");
            return "template";
        } else {
            return "redirect:/login?redirect=/settings";
        }
    }

    @GetMapping("error")
    public String getErrorLogs(Model model, HttpServletRequest request) {
        if (userAuthService.isAdministrator(model, request)) {
            model.addAttribute("error", new FileHandler("log.log").read());
            model.addAttribute("page", "errorLog");
            return "template";
        } else {
            return "redirect:/login?redirect=/settings/error";
        }
    }

    @PostMapping("clear")
    private String clearImportLogs(HttpServletRequest request) {
        if (userAuthService.isAdministrator(request)) {
            importLogDao.delete();
            return "redirect:/settings";
        } else {
            return "redirect:/login?redirect=/settings/error";
        }

    }

    @PostMapping("clearactivity")
    private String clearActivityLogs(HttpServletRequest request) {
        if (userAuthService.isAdministrator(request)) {
            activityLogDao.delete();
            return "redirect:/settings";
        } else {
            return "redirect:/login?redirect=/settings/error";
        }
    }

    @PostMapping("scedule")
    private String scedule(@RequestParam("time") String time, HttpServletRequest request) {
        if (userAuthService.isAdministrator(request)) {
            settingsService.setValue("restart", time);
            return "redirect:/settings?sceduled";
        } else {
            return "redirect:/login?redirect=/settings/error";
        }
    }

    @PostMapping("scedule/cancel")
    private String scedule(HttpServletRequest request) {
        if (userAuthService.isAdministrator(request)) {
            settingsService.setValue("restart", "0");
            return "redirect:/settings?canceled";
        } else {
            return "redirect:/login?redirect=/settings/error";
        }
    }
}
