package ch.moviescore.core.data.time;

import ch.moviescore.core.data.user.User;
import ch.moviescore.core.data.episode.Episode;
import ch.moviescore.core.data.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeRepository extends JpaRepository<Time, Long> {

    Time findTimeByUserAndMovie(User user, Movie movie);

    Time findTimeByUserAndEpisode(User user, Episode episode);

    List<Time> findTimesByUserOrderByTimestampDesc(User user);

}
