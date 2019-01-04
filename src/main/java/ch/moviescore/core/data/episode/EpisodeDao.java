package ch.moviescore.core.data.episode;

import ch.moviescore.core.data.DaoInterface;
import ch.moviescore.core.data.season.Season;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EpisodeDao implements DaoInterface<Episode> {

    private EpisodeRepository episodeRepository;

    public EpisodeDao(EpisodeRepository episodeRepository) {
        this.episodeRepository = episodeRepository;
    }

    @Override
    public Episode getById(Long id) {
        return episodeRepository.findEpisodeById(id);
    }

    @Override
    public List<Episode> getAll() {
        return episodeRepository.findAll();
    }

    @Override
    public void save(Episode episode) {
        episodeRepository.save(episode);
    }

    public List<Episode> getBySeason(Season season) {
        return episodeRepository.findEpisodesBySeasonOrderByEpisode(season);
    }
}
