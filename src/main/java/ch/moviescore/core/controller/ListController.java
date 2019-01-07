package ch.moviescore.core.controller;

import ch.moviescore.core.data.timeline.TimeLineDao;
import ch.moviescore.core.data.timeline.Timeline;
import ch.moviescore.core.service.auth.UserAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Wetwer
 * @project movie-db
 */
@Controller
@RequestMapping("list")
public class ListController {

    private TimeLineDao timeLineDto;

    private UserAuthService userAuthService;

    public ListController(TimeLineDao timeLineDto, UserAuthService userAuthService) {
        this.timeLineDto = timeLineDto;
        this.userAuthService = userAuthService;
    }

    @GetMapping(produces = "application/json")
    public List<Timeline> getLists(@RequestParam(name = "search", required = false, defaultValue = "") String search,
                                   Model model, HttpServletRequest request) {
        if (userAuthService.isUser(model, request)) {
            userAuthService.log(this.getClass(), request);
            return timeLineDto.searchTimeLine(search);
        } else {
            return null;
        }

    }

    @GetMapping(value = "{timelineId}", produces = "application/json")
    public Timeline getOneTimeLine(@PathVariable("timelineId") Long timeLineId,
                                   Model model, HttpServletRequest request) {
        if (userAuthService.isUser(model, request)) {
            userAuthService.log(this.getClass(), request);
            return timeLineDto.getById(timeLineId);
        } else {
            return null;
        }

    }
}
