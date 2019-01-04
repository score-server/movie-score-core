package ch.moviescore.core.service;

import ch.moviescore.core.data.episode.Episode;
import ch.moviescore.core.data.season.Season;
import ch.moviescore.core.data.serie.Serie;
import org.springframework.stereotype.Service;

/**
 * @author Wetwer
 * @project movie-db-api
 * @package ch.wetwer.moviedbapi.service
 * @created 28.11.2018
 **/

@Service
public class EpisodeService {
    public Episode getNextEpisode(Episode episode) {
        Season season = episode.getSeason();
        for (Episode nextEpisode : season.getEpisodes()) {
            if (nextEpisode.getEpisode() == episode.getEpisode() + 1) {
                return nextEpisode;
            }
        }

        Serie serie = season.getSerie();
        for (Season nextSeason : serie.getSeasons()) {
            if (nextSeason.getSeason() == season.getSeason() + 1)
                for (Episode nextEpisode : nextSeason.getEpisodes()) {
                    if (nextEpisode.getEpisode() == 1) {
                        return nextEpisode;
                    }
                }
        }
        return null;
    }
}
