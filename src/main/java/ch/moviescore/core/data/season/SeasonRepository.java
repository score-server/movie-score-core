package ch.moviescore.core.data.season;

import ch.moviescore.core.data.serie.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Wetwer
 * @project movie-db
 */
@Repository
public interface SeasonRepository extends JpaRepository<Season, Long> {

    Season findSeasonById(Long seasonId);

    Season findSeasonBySerieAndSeason(Serie serie, Integer season);

    List<Season> findSeasonsBySerieOrderBySeason(Serie serie);

}
