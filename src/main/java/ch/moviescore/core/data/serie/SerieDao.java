package ch.moviescore.core.data.serie;

import ch.moviescore.core.data.DaoInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SerieDao implements DaoInterface<Serie> {

    private SerieRepository serieRepository;

    public SerieDao(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    @Override
    public Serie getById(Long id) {
        return serieRepository.findSerieById(id);
    }

    @Override
    public List<Serie> getAll() {
        return serieRepository.findAll();
    }

    @Override
    public void save(Serie serie) {
        serieRepository.save(serie);
    }
}
