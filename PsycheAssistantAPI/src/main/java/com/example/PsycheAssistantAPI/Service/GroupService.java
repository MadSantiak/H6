package com.example.PsycheAssistantAPI.Service;

import com.example.PsycheAssistantAPI.Model.Activity;
import com.example.PsycheAssistantAPI.Model.Group;
import com.example.PsycheAssistantAPI.Model.User;
import com.example.PsycheAssistantAPI.Repository.ActivityRepository;
import com.example.PsycheAssistantAPI.Repository.GroupRepository;
import com.example.PsycheAssistantAPI.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Links Controller (end-points) to Repository (database)
 */
@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityRepository activityRepository;

    public List<Group> findAll() { return groupRepository.findAll(); }
    public Group findById(int id) { return groupRepository.findById(id).orElse(null); }
    public Group findByCode(String code) {
        return groupRepository.findByCode(code);
    }

    /**
     * Verifies the user exists, before creating a new group, generating its join-code (code), setting the owner, and saving it in the database.
     * @param userId
     * @return
     */
    public Group createGroup(int userId) {
        User creator = userRepository.findById(userId).orElse(null);
        if (creator == null) {
            throw new IllegalArgumentException("User not found");
        }

        Group group = new Group();
        group.setCode(generateUniqueCode());
        group.addUser(creator);
        group.setOwner(creator);


        return groupRepository.save(group);
    }

    /**
     * Adds a given user to a specific group, if found, matching the code passed.
     * @param code
     * @param user
     * @return
     */
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

        return group;
    }

    /**
     * Verifies the group and user exists, and that the user is a member of the group, before removing said user from group (and setting owner to null
     * if the user was the group owner).
     * @param groupId
     * @param userId
     * @return
     */
    public Group kickMember(int groupId, int userId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (!group.getUsers().contains(user)) {
            throw new RuntimeException("User is not a member of the group");
        }
        group.removeUser(user);

        if (user == group.getOwner())
        {
            group.setOwner(null);
        }

        groupRepository.save(group);

        return group;
    }

    /**
     * Delets the group after nulling group fields for its activities and users.
     * @param groupId
     */
    @Transactional
    public void deleteGroup(int groupId) {
        Group group = findById(groupId);
        if (group != null) {
            List<User> users = group.getUsers();
            for (User user : users) {
                user.setGroup(null);
                userRepository.save(user);
            }
            List<Activity> activities = group.getActivities();
            for (Activity activity : activities) {
                activity.setGroup(null);
                activityRepository.save(activity);
            }
            groupRepository.delete(group);
        }
    }

    /**
     * Helper function. Generates a random code, only using the first 6 characters of the generated UUID.
     * @return
     */
    private String generateUniqueCode() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid.substring(0, 6);
    }
}
