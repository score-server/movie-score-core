package ch.moviescore.core.data.importlog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Wetwer
 * @project movie-db
 */
@Repository
public interface ImportLogRepository extends JpaRepository<ImportLog, Long> {

    List<ImportLog> findAllByOrderByTimestampDesc();

}
