package com.example.PsycheAssistantAPI.Controller;

import com.example.PsycheAssistantAPI.Model.Group;
import com.example.PsycheAssistantAPI.Model.User;
import com.example.PsycheAssistantAPI.Repository.GroupRepository;
import com.example.PsycheAssistantAPI.Service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    private final GroupRepository repo;

    @Autowired
    private GroupService groupService;

    public GroupController(GroupRepository groupRepository) {
        this.repo = groupRepository;
    }

    @GetMapping("/{id}")
    public Group read(@PathVariable int id) {
        return repo.findById(id).orElse(null);
    }

    @GetMapping("/{id}/users")
    public List<User> getUsersInGroup(@PathVariable int id) {
        Group group = repo.findById(id).orElse(null);
        if (group == null) {
            return null;
        }
        return group.getUsers();
    }

    @PostMapping("/register")
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        try {
            Group newGroup = groupService.createGroup(group);
            return new ResponseEntity<>(newGroup, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
