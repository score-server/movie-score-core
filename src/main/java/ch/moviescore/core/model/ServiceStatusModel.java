package ch.moviescore.core.model;

import lombok.Data;

/**
 * @author Wetwer
 * @project movie-score-core
 * @package ch.moviescore.core.model
 * @created 08.01.2019
 **/

@Data
public class ServiceStatusModel {

    private String title;

    private String status;

    public ServiceStatusModel(String title, String status) {
        this.title = title;
        this.status = status;
    }

}
