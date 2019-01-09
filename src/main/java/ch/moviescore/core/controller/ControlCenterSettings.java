package ch.moviescore.core.controller;


import ch.moviescore.core.data.activitylog.ActivityLog;
import ch.moviescore.core.data.activitylog.ActivityLogDao;
import ch.moviescore.core.data.importlog.ImportLog;
import ch.moviescore.core.data.importlog.ImportLogDao;
import ch.moviescore.core.model.api.ControlCenterModel;
import ch.moviescore.core.service.auth.UserAuthService;
import ch.moviescore.core.service.filehandler.FileHandler;
import ch.moviescore.core.service.filehandler.SettingsService;
import io.swagger.annotations.Api;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Wetwer
 * @project movie-db
 */

@RestController
@RequestMapping("control")
@Api(value = "/control", description = "Control Center for Admins")
public class ControlCenterSettings {

    private ImportLogDao importLogDao;
    private ActivityLogDao activityLogDao;

    private SettingsService settingsService;
    private UserAuthService userAuthService;

    public ControlCenterSettings(SettingsService settingsService, UserAuthService userAuthService,
                                 ImportLogDao importLogDao, ActivityLogDao activityLogDao) {
        this.settingsService = settingsService;
        this.userAuthService = userAuthService;
        this.importLogDao = importLogDao;
        this.activityLogDao = activityLogDao;
    }

    @GetMapping(produces = "application/json")
    private @ResponseBody
    ControlCenterModel getControlCenter(Model model, HttpServletRequest request) {
        if (userAuthService.isAdministrator(model, request)) {
            ControlCenterModel controlCenterModel = new ControlCenterModel();
            controlCenterModel.setMoviePath(settingsService.getKey("moviePath"));
            controlCenterModel.setSeriePath(settingsService.getKey("seriePath"));
            controlCenterModel.setProgress(settingsService.getKey("importProgress"));
            controlCenterModel.setImportActive(settingsService.getKey("import").equals("1"));
            controlCenterModel.setRestartTime(settingsService.getKey("restart"));
            return controlCenterModel;
        } else {
            return null;
        }
    }

    @GetMapping(value = "importLogs", produces = "application/json")
    public List<ImportLog> getImportLogs(Model model, HttpServletRequest request) {
        if (userAuthService.isAdministrator(model, request)) {
            return importLogDao.getAll();
        } else {
            return null;
        }
    }

    @GetMapping(value = "activityLogs", produces = "application/json")
    public List<ActivityLog> getActivityLogs(Model model, HttpServletRequest request) {
        if (userAuthService.isAdministrator(model, request)) {
            return activityLogDao.getAll();
        } else {
            return null;
        }
    }

    @GetMapping("error")
    public String getErrorLogs(Model model, HttpServletRequest request) {
        if (userAuthService.isAdministrator(model, request)) {
            return new FileHandler("log.log").read();
        } else {
            return "AUTH_ERROR";
        }
    }

    @PostMapping("clearImport")
    private String clearImportLogs(HttpServletRequest request) {
        if (userAuthService.isAdministrator(request)) {
            importLogDao.delete();
            return "CLEAR";
        } else {
            return "AUTH_ERROR";
        }

    }

    @PostMapping("clearactivity")
    private String clearActivityLogs(HttpServletRequest request) {
        if (userAuthService.isAdministrator(request)) {
            activityLogDao.delete();
            return "CLEAR";
        } else {
            return "AUTH_ERROR";
        }
    }

    @PostMapping("scedule")
    private String scedule(@RequestParam("time") String time, HttpServletRequest request) {
        if (userAuthService.isAdministrator(request)) {
            settingsService.setValue("restart", time);
            return "SCEDULED";
        } else {
            return "AUTH_ERROR";
        }
    }

    @PostMapping("scedule/cancel")
    private String scedule(HttpServletRequest request) {
        if (userAuthService.isAdministrator(request)) {
            settingsService.setValue("restart", "0");
            return "CANCELED";
        } else {
            return "AUTH_ERROR";
        }
    }
}
