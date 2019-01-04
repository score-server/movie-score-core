package ch.moviescore.core.service;

import ch.moviescore.core.data.timeline.Timeline;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 * @author Wetwer
 * @project movie-db-api
 * @package ch.wetwer.moviedbapi.service
 * @created 28.11.2018
 **/

@Service
public class ListService {

    public void getNextListPlace(Model model, Timeline timeLine) {
        try {
            model.addAttribute("nextListPlace",
                    timeLine.getListMovies().get(timeLine.getListMovies().size() - 1).getPlace() + 1);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            model.addAttribute("nextListPlace", 1);
        }
    }

}
