package com.example.PsycheAssistantAPI.Service;

import com.example.PsycheAssistantAPI.Model.Group;
import com.example.PsycheAssistantAPI.Model.User;
import com.example.PsycheAssistantAPI.Repository.GroupRepository;
import com.example.PsycheAssistantAPI.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Group> findAll() { return groupRepository.findAll(); }
    public Group findById(int id) { return groupRepository.findById(id).orElse(null); }
    public Group createGroup(int userId) {
        User creator = userRepository.findById(userId).orElse(null);
        if (creator == null) {
            throw new IllegalArgumentException("User not found");
        }

        Group group = new Group();
        group.setCode(generateUniqueCode());
        group.addUser(creator);

        Group newGroup = groupRepository.save(group);

        creator.setGroup(newGroup);
        userRepository.save(creator);

        return newGroup;
    }

    private String generateUniqueCode() {
        return "TestCode";
    }
}
