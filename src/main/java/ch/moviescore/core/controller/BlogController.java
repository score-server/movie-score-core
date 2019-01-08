package ch.moviescore.core.controller;

import ch.moviescore.core.data.blog.Blog;
import ch.moviescore.core.data.blog.BlogDao;
import ch.moviescore.core.data.user.User;
import ch.moviescore.core.service.ActivityService;
import ch.moviescore.core.service.auth.UserAuthService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("blog")
public class BlogController {

    private BlogDao blogDao;

    private UserAuthService userAuthService;
    private ActivityService activityService;

    public BlogController(BlogDao blogDao, UserAuthService userAuthService, ActivityService activityService) {
        this.blogDao = blogDao;
        this.userAuthService = userAuthService;
        this.activityService = activityService;
    }

    @GetMapping(produces = "application/json")
    public @ResponseBody
    List<Blog> getBlogList(Model model, HttpServletRequest request) {
        if (userAuthService.isUser(model, request)) {
            userAuthService.log(this.getClass(), request);
            return blogDao.getAll();
        } else {
            return null;
        }
    }

    @PostMapping("create")
    public String saveNewPost(@RequestParam("title") String title,
                              @RequestParam("text") String text, Model model, HttpServletRequest request) {
        if (userAuthService.isAdministrator(model, request)) {
            userAuthService.log(this.getClass(), request);
            User user = userAuthService.getUser(request).getUser();
            blogDao.createBlog(title, text, user);
            activityService.log(user.getName() + " created new Blog Post", user);
            return "SAVED";
        } else {
            return "AUTH_ERROR";
        }
    }

    @PostMapping("{blogId}/delete")
    public String deleteBlog(@PathVariable("blogId") Long blogId,
                             Model model, HttpServletRequest request) {
        if (userAuthService.isAdministrator(model, request)) {
            blogDao.delete(blogDao.getById(blogId));
            return "DELETED";
        } else {
            return "AUTH_ERROR";
        }

    }
}
