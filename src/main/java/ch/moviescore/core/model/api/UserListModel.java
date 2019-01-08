package ch.moviescore.core.model.api;

import ch.moviescore.core.data.user.User;
import lombok.Data;

import java.util.List;

/**
 * @author Wetwer
 * @project movie-score-core
 * @package ch.moviescore.core.model.api
 * @created 08.01.2019
 **/

@Data
public class UserListModel {

    private List<User> userList;

    private String search;

}
