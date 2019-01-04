package ch.moviescore.core.service.auth;

import ch.moviescore.core.data.session.Session;
import ch.moviescore.core.data.session.SessionDao;
import ch.moviescore.core.data.user.User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class SessionService {

    private SessionDao sessionDto;

    public SessionService(SessionDao sessionDto) {
        this.sessionDto = sessionDto;
    }

    public void addSession(User user, String sessionId) {
        Session session = new Session();
        session.setSessionId(sessionId);
        session.setUser(user);
        session.setActive(true);
        session.setTimestamp(new Timestamp(new Date().getTime()));
        sessionDto.save(session);
    }

    public void logout(String sessionId) {
        sessionDto.deactivate(sessionDto.getBySessionId(sessionId));
    }
}
