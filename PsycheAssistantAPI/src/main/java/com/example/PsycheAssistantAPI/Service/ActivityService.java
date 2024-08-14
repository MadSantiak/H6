package com.example.PsycheAssistantAPI.Service;

import com.example.PsycheAssistantAPI.Model.Activity;
import com.example.PsycheAssistantAPI.Model.Group;
import com.example.PsycheAssistantAPI.Model.User;
import com.example.PsycheAssistantAPI.Repository.ActivityRepository;
import com.example.PsycheAssistantAPI.Repository.GroupRepository;
import com.example.PsycheAssistantAPI.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    public List<Activity> findAll() { return activityRepository.findAll(); }
    public Activity findById(int id) { return activityRepository.findById(id).orElse(null); }


    public Activity createActivity(int userId, int groupId, String deadline) {
        User creator = userRepository.findById(userId).orElse(null);
        Group targetGroup = groupRepository.findById(groupId).orElse(null);
        if (creator == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (targetGroup == null) {
            throw new IllegalArgumentException("Group not found");
        }

        Activity activity = new Activity();
        try {
            LocalDateTime deadlineDateTime = LocalDateTime.parse(deadline);
            activity.setDeadline(deadlineDateTime);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid deadline format");
        }
        activity.setOwner(creator);

        Activity newActivity = activityRepository.save(activity);

        targetGroup.addActivity(newActivity);
        groupRepository.save(targetGroup);

        return newActivity;
    }


}
