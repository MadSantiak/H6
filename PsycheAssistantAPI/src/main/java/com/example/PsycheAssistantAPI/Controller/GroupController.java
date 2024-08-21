package com.example.PsycheAssistantAPI.Controller;

import com.example.PsycheAssistantAPI.Helper.AuthHelper;
import com.example.PsycheAssistantAPI.Model.Group;
import com.example.PsycheAssistantAPI.Model.User;
import com.example.PsycheAssistantAPI.Security.JwtUtil;
import com.example.PsycheAssistantAPI.Service.GroupService;
import com.example.PsycheAssistantAPI.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthHelper authHelper;

    @GetMapping()
    public List<Group> getAllGroups() { return groupService.findAll(); }

    @GetMapping("/{id}")
    public Group read(@PathVariable int id) {
        return groupService.findById(id);
    }

    @GetMapping("/{id}/users")
    public List<User> getUsersInGroup(@PathVariable int id) {
        Group group = groupService.findById(id);
        if (group == null) {
            return null;
        }
        return group.getUsers();
    }

    @PostMapping("/register")
    public ResponseEntity<Group> createGroup(@RequestHeader("Authorization") String authHeader) {
        ResponseEntity<User> userResponse = authHelper.validateAndGetUser(authHeader);
        if (userResponse.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(null, userResponse.getStatusCode());
        }

        User user = userResponse.getBody();
        assert user != null;
        Group newGroup = groupService.createGroup(user.getId());
        return new ResponseEntity<>(newGroup, HttpStatus.CREATED);
    }

    @PostMapping("/join")
    public ResponseEntity<Group> joinGroup(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody Map<String, String> payload) {
        String code = payload.get("code");
        ResponseEntity<User> userResponse = authHelper.validateAndGetUser(authHeader);
        User user = userResponse.getBody();
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            Group updatedGroup = groupService.joinGroup(code, user);
            return new ResponseEntity<>(updatedGroup, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/kick")
    public ResponseEntity<String> kickMember(@RequestHeader("Authorization") String authHeader,
                                             @PathVariable int id,
                                             @RequestBody Map<String, Integer> payload) {
        Integer userIdToKick = payload.get("userId");
        ResponseEntity<User> userResponse = authHelper.validateAndGetUser(authHeader);
        User user = userResponse.getBody();
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            Group group = groupService.findById(id);
            if (group == null) {
                return new ResponseEntity<>("Group not found", HttpStatus.NOT_FOUND);
            }

            // Check if the current user is the owner of the group
            if (group.getOwner().getId() != user.getId()) {
                return new ResponseEntity<>("Only the group owner can kick members", HttpStatus.FORBIDDEN);
            }

            // Proceed with kicking the member
            Group updatedGroup = groupService.kickMember(id, userIdToKick);
            return new ResponseEntity<>("User kicked successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/leave")
    public ResponseEntity<Boolean> leaveGroup(@RequestHeader("Authorization") String authHeader,
                                              @PathVariable int id) {
        ResponseEntity<User> userResponse = authHelper.validateAndGetUser(authHeader);
        User user = userResponse.getBody();
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            Group group = groupService.findById(id);
            if (group == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            groupService.kickMember(id, user.getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}/disband")
    public ResponseEntity<Boolean> disbandGroup(@RequestHeader("Authorization") String authHeader,
                                              @PathVariable int id) {
        ResponseEntity<User> userResponse = authHelper.validateAndGetUser(authHeader);
        User user = userResponse.getBody();
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            groupService.deleteGroup(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
