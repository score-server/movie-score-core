package ch.moviescore.core.data.listmovie;


import ch.moviescore.core.data.DaoInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListMovieDao implements DaoInterface<ListMovie> {

    private ListMovieRepository listMovieRepository;

    public ListMovieDao(ListMovieRepository listMovieRepository) {
        this.listMovieRepository = listMovieRepository;
    }

    @Override
    public ListMovie getById(Long id) {
        return listMovieRepository.findListMovieById(id);
    }

    @Override
    public List<ListMovie> getAll() {
        return listMovieRepository.findAll();
    }

    @Override
    public void save(ListMovie listMovie) {
        listMovieRepository.save(listMovie);
    }

    public void delete(ListMovie listMovie) {
        listMovieRepository.delete(listMovie);
    }
}
