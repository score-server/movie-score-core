package ch.moviescore.core.model;

import ch.moviescore.core.data.time.Time;
import lombok.Data;

@Data
public class StartedVideo {

    private VideoModel videoModel;
    private float progress;
    private Time time;

}
