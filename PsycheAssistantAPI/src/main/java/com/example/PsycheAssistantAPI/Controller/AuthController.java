package com.example.PsycheAssistantAPI.Controller;

import com.example.PsycheAssistantAPI.Model.User;
import com.example.PsycheAssistantAPI.Security.JwtUtil;
import com.example.PsycheAssistantAPI.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String email, @RequestParam String password) {
        User user = userService.findByEmail(email);
        if (user != null && new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            String token = jwtUtil.generateToken(email);
            token = token;
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
