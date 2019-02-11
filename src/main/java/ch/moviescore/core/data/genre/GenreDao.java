package ch.moviescore.core.data.genre;

import ch.moviescore.core.data.DaoInterface;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreDao implements DaoInterface<Genre> {

    private GenreRepository genreRepository;

    public GenreDao(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Genre getById(Long id) {
        return genreRepository.getOne(id);
    }

    @Override
    public List<Genre> getAll() {
        return genreRepository.findGenreByOrderByName();
    }

    @Override
    public void save(Genre genre) {
        genreRepository.save(genre);
    }

    public List<Genre> getForMovies() {
        return genreRepository.findGenresByMovieNotNullOrderByName();
    }

    public List<Genre> getForSeries() {
        return genreRepository.findGenresBySerieNotNullOrderByName();
    }
}
