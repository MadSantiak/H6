package com.example.PsycheAssistantAPI.Service;

import com.example.PsycheAssistantAPI.Model.Group;
import com.example.PsycheAssistantAPI.Model.User;
import com.example.PsycheAssistantAPI.Repository.GroupRepository;
import com.example.PsycheAssistantAPI.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Group> findAll() { return groupRepository.findAll(); }
    public Group findById(int id) { return groupRepository.findById(id).orElse(null); }
    public Group findByCode(String code) {
        return groupRepository.findByCode(code);
    }

    public Group createGroup(int userId) {
        User creator = userRepository.findById(userId).orElse(null);
        if (creator == null) {
            throw new IllegalArgumentException("User not found");
        }

        Group group = new Group();
        group.setCode(generateUniqueCode());
        group.addUser(creator);
        group.setOwner(creator);

        Group newGroup = groupRepository.save(group);

        creator.setGroup(newGroup);
        userRepository.save(creator);

        return newGroup;
    }

    public Group joinGroup(String code, User user) {
        Group group = findByCode(code);
        if (group == null) {
            throw new IllegalArgumentException("Group not found");
        }

        if (group.getUsers().contains(user)) {
            throw new IllegalArgumentException("User is already in this group");
        }

        group.addUser(user);
        groupRepository.save(group);
        userRepository.save(user);

        return group;
    }

    public Group kickMember(int groupId, int userId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (!group.getUsers().contains(user)) {
            throw new RuntimeException("User is not a member of the group");
        }
        group.removeUser(user);
        userRepository.save(user);
        groupRepository.save(group);

        return group;
    }

    private String generateUniqueCode() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid.substring(0, 6);
    }
}
