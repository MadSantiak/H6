package com.example.PsycheAssistantAPI.Controller;

import com.example.PsycheAssistantAPI.Helper.AuthHelper;
import com.example.PsycheAssistantAPI.Model.Activity;
import com.example.PsycheAssistantAPI.Model.Group;
import com.example.PsycheAssistantAPI.Model.User;
import com.example.PsycheAssistantAPI.Security.JwtUtil;
import com.example.PsycheAssistantAPI.Service.ActivityService;
import com.example.PsycheAssistantAPI.Service.GroupService;
import com.example.PsycheAssistantAPI.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controller for API; exposes back-ends for front- to back-end communication used for manipulating Activity model data.
 */
@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthHelper authHelper;


    @GetMapping()
    public List<Activity> getAllActivities() {
        return activityService.findAll();
    }

    @GetMapping("/{id}")
    public Activity read(@PathVariable int id) {
        return activityService.findById(id);
    }

    /**
     * Validates user and creates an activity based on the payload contents received.
     * @param authHeader
     * @param payload
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity<Activity> createActivity(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> payload) {

        String deadline = (String) payload.get("deadline");
        String description = (String) payload.get("description");
        int energyCost = Integer.parseInt((String) payload.get("energyCost"));

        // Validate user authorization
        ResponseEntity<User> userResponse = authHelper.validateAndGetUser(authHeader);
        if (userResponse.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(null, userResponse.getStatusCode());
        }

        User user = userResponse.getBody();
        assert user != null;

        try {
            Activity newActivity = activityService.createActivity(user, deadline, description, energyCost);
            return new ResponseEntity<>(newActivity, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Validates the user and sets the activity to completed by that user
     * @param authHeader
     * @param id
     * @return
     */
    @PostMapping("/{id}/complete")
    public ResponseEntity<Activity> completeActivity(@RequestHeader("Authorization") String authHeader, @PathVariable int id) {
        ResponseEntity<User> userResponse = authHelper.validateAndGetUser(authHeader);
        User user = userResponse.getBody();
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            Activity completedActivity = activityService.completeActivity(user, id);
            return new ResponseEntity<>(completedActivity, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * Deletes an activity from the database
     * @param authHeader
     * @param id
     * @return
     */
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Boolean> deleteActivity(@RequestHeader("Authorization") String authHeader, @PathVariable int id) {
        ResponseEntity<User> userResponse = authHelper.validateAndGetUser(authHeader);
        User user = userResponse.getBody();
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        boolean success = activityService.deleteActivity(id);
        return success ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Gets a range of activities for a group, based on the specific date they were created.
     * @param groupId
     * @param date
     * @return
     */
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<Activity>> getActivitiesForGroupWithDeadline(
            @PathVariable int groupId,
            @RequestParam("date") String date) {
        List<Activity> activities = activityService.getActivitiesForGroupWithDeadline(groupId, date);
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    /**
     * Gets a range of activities where their deadline (creation date) is within a given period.
     * @param groupId
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/group/{groupId}/period")
    public ResponseEntity<List<Activity>> getPeriodActivitiesWithDeadline(
            @PathVariable int groupId,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {

        List<Activity> activities = activityService.getByPeriod(groupId, startDate, endDate);
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    /**
     * Gets a range of activities where their handledOn date is within a given period.
     * @param groupId
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/group/{groupId}/handled/period")
    public ResponseEntity<List<Activity>> getPeriodActivitiesWithHandled(
            @PathVariable int groupId,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {

        List<Activity> activities = activityService.getHandledByPeriod(groupId, startDate, endDate);
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

}