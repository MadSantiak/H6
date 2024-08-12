package com.example.PsycheAssistantAPI.Controller;

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

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;


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
        try {
            String token = authHeader.replace("Bearer ", "");
            String email = jwtUtil.extractEmail(token);
            User user = userService.findByEmail(email);

            if (user == null) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }

            Group newGroup = groupService.createGroup(user.getId());
            return new ResponseEntity<>(newGroup, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
