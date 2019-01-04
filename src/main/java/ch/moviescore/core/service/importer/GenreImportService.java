package ch.moviescore.core.service.importer;


import ch.moviescore.core.data.genre.Genre;
import ch.moviescore.core.data.genre.GenreDao;
import ch.moviescore.core.data.movie.Movie;
import ch.moviescore.core.data.serie.Serie;
import ch.moviescore.core.model.tmdb.GenreJson;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Wetwer
 * @project movie-db
 */
@Service
public class GenreImportService {

    private GenreDao genreDao;

    public GenreImportService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public void setGenre(Movie movie, List<GenreJson> genreList) {
        for (GenreJson genreJson : genreList) {
            Genre genre = new Genre();
            genre.setName(genreJson.getName());
            genre.setMovie(movie);
            genreDao.save(genre);
        }

    }

    public void setGenre(Serie serie, List<GenreJson> genreList) {
        for (GenreJson genreJson : genreList) {
            Genre genre = new Genre();
            genre.setName(genreJson.getName());
            genre.setSerie(serie);
            genreDao.save(genre);
        }

    }
}
