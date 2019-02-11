package ch.moviescore.core.data.season;


import ch.moviescore.core.data.DaoInterface;
import ch.moviescore.core.data.serie.Serie;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeasonDao implements DaoInterface<Season> {

    private SeasonRepository seasonRepository;

    public SeasonDao(SeasonRepository seasonRepository) {
        this.seasonRepository = seasonRepository;
    }

    @Override
    public Season getById(Long id) {
        return seasonRepository.findSeasonById(id);
    }

    @Override
    public List<Season> getAll() {
        return seasonRepository.findAll();
    }

    @Override
    public void save(Season season) {
        seasonRepository.save(season);
    }

    public List<Season> getBySerie(Serie serie) {
        return seasonRepository.findSeasonsBySerieOrderBySeason(serie);
    }
}
