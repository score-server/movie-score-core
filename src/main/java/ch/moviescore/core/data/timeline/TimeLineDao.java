package ch.moviescore.core.data.timeline;



import ch.moviescore.core.data.DaoInterface;
import ch.moviescore.core.data.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeLineDao implements DaoInterface<Timeline> {

    private TimelineRepository timelineRepository;

    public TimeLineDao(TimelineRepository timelineRepository) {
        this.timelineRepository = timelineRepository;
    }

    @Override
    public Timeline getById(Long id) {
        return timelineRepository.findTimelineById(id);
    }

    @Override
    public List<Timeline> getAll() {
        return timelineRepository.findAll();
    }

    @Override
    public void save(Timeline timeline) {
        timelineRepository.save(timeline);
    }

    public List<Timeline> searchTimeLine(String search) {
        return timelineRepository.findTimelinesByTitleContaining(search);
    }

    public void delete(Timeline timeline) {
        timelineRepository.delete(timeline);
    }

    public List<Timeline> getByUser(User user) {
        return timelineRepository.findTimelinesByUser(user);
    }
}
