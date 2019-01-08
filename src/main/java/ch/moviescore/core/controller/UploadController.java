package ch.moviescore.core.controller;

import ch.moviescore.core.service.auth.UserAuthService;
import ch.moviescore.core.service.filehandler.SettingsService;
import org.apache.commons.io.FileUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Wetwer
 * @project movie-db-api
 * @package ch.wetwer.moviedbapi.controller
 * @created 27.12.2018
 **/

@RestController
@RequestMapping("upload")
public class UploadController {

    private SettingsService settingsService;
    private UserAuthService userAuthService;

    public UploadController(SettingsService settingsService, UserAuthService userAuthService) {
        this.settingsService = settingsService;
        this.userAuthService = userAuthService;
    }

    @GetMapping
    public List<File> getUploadPage(Model model, HttpServletRequest request) {
        if (userAuthService.isAdministrator(model, request)) {
            File file = new File(settingsService.getKey("moviePath") + "_tmp");
            return new ArrayList<>(Arrays.asList(file.listFiles()));
        } else {
            return null;
        }
    }

    @PostMapping("movie")
    public String upload(@RequestParam("movie") MultipartFile multipartFile,
                         HttpServletRequest request) throws IOException {
        if (userAuthService.isAdministrator(request)) {
            InputStream fileStream = multipartFile.getInputStream();
            File targetFile = new File(
                    settingsService.getKey("moviePath") + "_tmp/" + multipartFile.getOriginalFilename());
            FileUtils.copyInputStreamToFile(fileStream, targetFile);
            return "UPLOADED";
        } else {
            return "AUTH_ERROR";
        }
    }

    @PostMapping("edit/{hash}")
    public String changeFileName(@PathVariable("hash") int hash, @RequestParam("name") String newName,
                                 HttpServletRequest request) {
        if (userAuthService.isAdministrator(request)) {
            File file = new File(settingsService.getKey("moviePath") + "_tmp");
            for (File fi : file.listFiles()) {
                if (fi.hashCode() == hash) {
                    try {
                        File newFile = new File(fi.getParent(), newName);
                        Files.move(fi.toPath(), newFile.toPath());
                        return "NAME_CHANGED";
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return "NOK";
        }
        return "AUTH_ERROR";
    }

    @PostMapping("accept/{hash}")
    public String acceptMovie(@PathVariable("hash") int hash, HttpServletRequest request) {
        if (userAuthService.isAdministrator(request)) {
            File file = new File(settingsService.getKey("moviePath") + "_tmp");
            for (File fi : file.listFiles()) {
                if (fi.hashCode() == hash) {
                    try {
                        File movedFile = new File(settingsService.getKey("moviePath") + "/" + fi.getName());
                        Files.move(fi.toPath(), movedFile.toPath());
                        return "ACCEPTED";
                    } catch (FileAlreadyExistsException e) {
                        return "ALLREADY_EXISTS";
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "AUTH_ERROR";
    }

    @PostMapping("delete/{hash}")
    public String deleteFile(@PathVariable("hash") int hash, HttpServletRequest request) {
        if (userAuthService.isAdministrator(request)) {
            File file = new File(settingsService.getKey("moviePath") + "_tmp");
            for (File fi : file.listFiles()) {
                if (fi.hashCode() == hash) {
                    return "DELETED";
                }
            }
        }
        return "AUTH_ERROR";
    }
}
