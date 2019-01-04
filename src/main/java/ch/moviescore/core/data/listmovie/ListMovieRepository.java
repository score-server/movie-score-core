package ch.moviescore.core.data.listmovie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Wetwer
 * @project movie-db
 */
@Repository
public interface ListMovieRepository extends JpaRepository<ListMovie, Long> {

    ListMovie findListMovieById(Long id);

}
