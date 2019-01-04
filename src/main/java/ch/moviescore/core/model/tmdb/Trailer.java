package ch.moviescore.core.model.tmdb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @author Wetwer
 * @project movie-db
 */
@Data
public class Trailer {

    @SerializedName("key")
    @Expose
    private String key;
}
