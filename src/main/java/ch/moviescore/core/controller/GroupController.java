package ch.moviescore.core.controller;

import ch.moviescore.core.data.groupinvite.GroupDao;
import ch.moviescore.core.data.groupinvite.GroupInvite;
import ch.moviescore.core.data.user.User;
import ch.moviescore.core.data.user.UserDao;
import ch.moviescore.core.model.GroupModel;
import ch.moviescore.core.service.auth.UserAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Wetwer
 * @project movie-db
 */

@Controller
@RequestMapping("group")
public class GroupController {

    private GroupDao groupDao;
    private UserDao userDao;

    private UserAuthService userAuthService;

    public GroupController(GroupDao groupDao, UserDao userDao, UserAuthService userAuthService) {
        this.groupDao = groupDao;
        this.userDao = userDao;
        this.userAuthService = userAuthService;
    }

    @GetMapping
    public List<GroupModel> getGroupList(HttpServletRequest request, Model model) {
        if (userAuthService.isAdministrator(model, request)) {
            userAuthService.log(this.getClass(), request);

            List<GroupModel> groupModelList = new ArrayList<>();

            for (GroupInvite group : groupDao.getAll()) {
                GroupModel groupModel = new GroupModel();
                groupModel.setGroup(group);
                groupModel.setUsers(group.getUsers());
                groupModelList.add(groupModel);
            }

            return groupModelList;
        } else {
            return null;
        }
    }

    @PostMapping("/delete/{groupId}")
    public String deleteGroup(@PathVariable Long groupId, HttpServletRequest request) {
        if (userAuthService.isAdministrator(request)) {
            GroupInvite group = groupDao.getById(groupId);
            if (group.isActive()) {
                group.setActive(false);
            } else {
                group.setActive(true);
            }
            groupDao.save(group);
            return "DEACTIVATED";
        }
        return "AUTH_ERROR";
    }

    @PostMapping("/new")
    public String saveGroup(@RequestParam("name") String name, HttpServletRequest request) {
        if (userAuthService.isAdministrator(request)) {
            GroupInvite groupInvite = new GroupInvite();
            groupInvite.setName(name);
            groupDao.save(groupInvite);
            return "SAVED";
        }
        return "AUTH_ERROR";

    }

    @PostMapping("remove/{userId}")
    public String removeUser(@PathVariable("userId") Long userId, HttpServletRequest request) {
        if (userAuthService.isAdministrator(request)) {
            User user = userDao.getById(userId);
            user.setGroup(null);
            userDao.save(user);
            return "REMOVED";
        }
        return "AUTH_ERROR";

    }

    @PostMapping("{groupId}/add")
    public String addUser(@PathVariable("groupId") Long groupId, @RequestParam("name") String name,
                          HttpServletRequest request) {
        if (userAuthService.isAdministrator(request)) {
            try {
                User user = userDao.getByName(name);
                user.setGroup(groupDao.getById(groupId));
                userDao.save(user);
                return "ADDED";
            } catch (NullPointerException e) {
                return "NOT_EXIST";
            }
        }
        return "AUTH_ERROR";
    }
}
