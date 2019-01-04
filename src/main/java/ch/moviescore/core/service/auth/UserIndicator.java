package ch.moviescore.core.service.auth;

import ch.moviescore.core.data.user.User;
import lombok.Data;

/**
 * @author Wetwer
 * @project movie-db
 */

@Data
public class UserIndicator {

    private boolean loggedIn;

    private User user;

}
