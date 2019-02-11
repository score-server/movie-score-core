package ch.moviescore.core.data.likes;



import ch.moviescore.core.data.movie.Movie;
import ch.moviescore.core.data.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Wetwer
 * @project movie-db
 */
@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

    Likes findLikeByUserAndMovie(User user, Movie movie);

    void deleteLikesByUser(User user);

}
