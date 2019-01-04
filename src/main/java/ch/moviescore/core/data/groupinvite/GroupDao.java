package ch.moviescore.core.data.groupinvite;

import ch.moviescore.core.data.DaoInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupDao implements DaoInterface<GroupInvite> {

    private GroupRepository groupRepository;

    public GroupDao(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public GroupInvite getById(Long id) {
        return groupRepository.getOne(id);
    }

    public List<GroupInvite> getAll() {
        return groupRepository.findAll();
    }

    @Override
    public void save(GroupInvite groupInvite) {
        groupRepository.save(groupInvite);
    }

    public void delete(Long groupId) {
        groupRepository.delete(groupRepository.getOne(groupId));
    }

    public GroupInvite getByName(String groupKey) {
        return groupRepository.findByNameEquals(groupKey);
    }
}
