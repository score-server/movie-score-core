package ch.moviescore.core.data.comment;


import ch.moviescore.core.data.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * @author Wetwer
 * @project movie-db
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    void deleteCommentByUser(User user);

}
