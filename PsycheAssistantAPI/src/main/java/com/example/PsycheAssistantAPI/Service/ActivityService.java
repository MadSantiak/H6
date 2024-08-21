package com.example.PsycheAssistantAPI.Service;

import com.example.PsycheAssistantAPI.Model.Activity;
import com.example.PsycheAssistantAPI.Model.Group;
import com.example.PsycheAssistantAPI.Model.User;
import com.example.PsycheAssistantAPI.Repository.ActivityRepository;
import com.example.PsycheAssistantAPI.Repository.GroupRepository;
import com.example.PsycheAssistantAPI.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

    public List<Activity> findAll() {
        return activityRepository.findAll();
    }

    public Activity findById(int id) {
        return activityRepository.findById(id).orElse(null);
    }

    public Activity createActivity(User user, String deadline, String description, int energyCost) {
        Group targetGroup = groupRepository.findById(user.getGroup().getId()).orElseThrow(() -> new IllegalArgumentException("Group not found"));

        LocalDate deadlineDate = LocalDate.parse(deadline, DateTimeFormatter.ISO_LOCAL_DATE);

        Activity activity = new Activity(user, description, energyCost, deadlineDate);
        Activity newActivity = activityRepository.save(activity);

        targetGroup.addActivity(newActivity);
        groupRepository.save(targetGroup);

        return newActivity;
    }

    public Activity completeActivity(User user, int activityId) {
        Activity activity = findById(activityId);
        if (activity.isCompleted()) {
            throw new IllegalStateException("Activity is already completed");
        }

        activity.setCompleted(true);
        activity.setHandledBy(user);
        activity.setHandledOn(LocalDate.now());
        activityRepository.save(activity);
        return activity;

    }

    public boolean deleteActivity(int activityId) {
        Activity activity = findById(activityId);
        if (activity != null) {
            activityRepository.delete(activity);
            return true;
        }
        return false;
    }

    public List<Activity> getActivitiesForGroupWithDeadline(int groupId, String date) {
        LocalDate dateBoundary = LocalDate.parse(date);
        return activityRepository.findActivitiesForGroupWithDeadline(groupId, dateBoundary);
    }

    public List<Activity> getByPeriod(int groupId, String startDate, String endDate) {
        LocalDate dateStart = LocalDate.parse(startDate);
        LocalDate dateEnd = LocalDate.parse(endDate);
        return activityRepository.findActivitiesForPeriod(groupId, dateStart, dateEnd);
    }

    public List<Activity> getHandledByPeriod(int groupId, String startDate, String endDate) {
        LocalDate dateStart = LocalDate.parse(startDate);
        LocalDate dateEnd = LocalDate.parse(endDate);
        return activityRepository.findHandledActivitiesForPeriod(groupId, dateStart, dateEnd);
    }
}