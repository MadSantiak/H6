package com.example.PsycheAssistantAPI.Controller;

import com.example.PsycheAssistantAPI.Model.User;
import com.example.PsycheAssistantAPI.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository repo;

    public UserController(UserRepository userRepository) {
        this.repo = userRepository;
    }

    @GetMapping()
    public List<User> getAllUsers() { return repo.findAll(); }

    @GetMapping("/{id}")
    public User read(@PathVariable int id) {
        return repo.findById(id).get();
    }

    @PostMapping()
    public ResponseEntity<User> create(@RequestBody User user) {
        User newUser = repo.save(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
}
