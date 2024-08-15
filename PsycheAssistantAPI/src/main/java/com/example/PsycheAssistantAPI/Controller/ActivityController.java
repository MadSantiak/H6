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
    public List<Activity> getAllActivities() { return activityService.findAll(); }

    @GetMapping("/{id}")
    public Activity read(@PathVariable int id) {
        return activityService.findById(id);
    }

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

    @PostMapping("/{id}/complete")
    public ResponseEntity<Void> completeActivity(@PathVariable int id) {
        boolean success = activityService.completeActivity(id);
        return success ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteActivity(@PathVariable int id) {
        boolean success = activityService.deleteActivity(id);
        return success ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<Activity>> getActivitiesForGroupWithDeadline(
            @PathVariable int groupId,
            @RequestParam("date") String date) {
        List<Activity> activities = activityService.getActivitiesForGroupWithDeadline(groupId, date);
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }
}