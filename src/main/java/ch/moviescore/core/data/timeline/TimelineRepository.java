package ch.moviescore.core.data.timeline;


import ch.moviescore.core.data.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Wetwer
 * @project movie-db
 */
@Repository
public interface TimelineRepository extends JpaRepository<Timeline, Long> {

    List<Timeline> findTimelinesByTitleContaining(String search);

    List<Timeline> findTimelinesByUser(User user);

    Timeline findTimelineById(Long id);

}
