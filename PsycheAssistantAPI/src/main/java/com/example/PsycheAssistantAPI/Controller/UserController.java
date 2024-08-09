package com.example.PsycheAssistantAPI.Controller;

import com.example.PsycheAssistantAPI.Model.User;
import com.example.PsycheAssistantAPI.Repository.UserRepository;
import com.example.PsycheAssistantAPI.Security.JwtUtil;
import com.example.PsycheAssistantAPI.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserRepository userRepository, UserService userService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping()
    public List<User> getAllUsers() { return userRepository.findAll(); }

    @GetMapping("/{id}")
    public User read(@PathVariable int id) {
        return userRepository.findById(id).get();
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

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String authToken) {
        try {
            // Extract the token (Assuming Bearer Token format)
            String token = authToken.replace("Bearer ", "");
            String email = jwtUtil.extractUsername(token);  // Extract email or ID from token

            User user = userService.findByEmail(email);

            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
