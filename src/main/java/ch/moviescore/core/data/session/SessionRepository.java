package ch.moviescore.core.data.session;

import ch.moviescore.core.data.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Session findSessionBySessionIdAndActive(String sessionId, Boolean active);

    List<Session> findSessionsByUserAndActiveOrderByTimestamp(User user, Boolean active);

}
