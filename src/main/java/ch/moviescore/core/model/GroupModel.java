package ch.moviescore.core.model;

import ch.moviescore.core.data.user.User;
import lombok.Data;

import java.util.List;

/**
 * @author Wetwer
 * @project movie-db-api
 * @package ch.wetwer.moviedbapi.model
 * @created 17.12.2018
 **/
@Data
public class GroupModel {

    private Long id;

    private String name;

    private List<User> users;

}
