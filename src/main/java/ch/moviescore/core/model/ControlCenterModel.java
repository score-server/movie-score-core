package ch.moviescore.core.model;

import lombok.Data;

/**
 * @author Wetwer
 * @project movie-score
 * @package ch.moviescore.core.model
 * @created 04.01.2019
 **/

@Data
public class ControlCenterModel {

    private String moviePath;

    private String seriePath;

    private String progress;

    private String restartTime;

    private boolean importActive;

}
