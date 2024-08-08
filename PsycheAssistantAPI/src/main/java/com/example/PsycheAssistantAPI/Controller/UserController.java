package com.example.PsycheAssistantAPI.Controller;

import com.example.PsycheAssistantAPI.Model.User;
import com.example.PsycheAssistantAPI.Repository.UserRepository;
import com.example.PsycheAssistantAPI.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository repo;

    @Autowired
    private UserService userService;

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

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestParam String email, @RequestParam String password) {
        try {
            userService.registerUser(email, password);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error registering user");
        }
    }
}
