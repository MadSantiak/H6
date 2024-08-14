package com.example.PsycheAssistantAPI.Helper;

import com.example.PsycheAssistantAPI.Model.User;
import com.example.PsycheAssistantAPI.Service.UserService;
import com.example.PsycheAssistantAPI.Security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthHelper {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthHelper(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    public String extractToken(String authHeader) throws IllegalArgumentException {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid authorization header");
        }
        return authHeader.replace("Bearer ", "");
    }

    public String getEmailFromToken(String authHeader) throws IllegalArgumentException {
        String token = extractToken(authHeader);
        return jwtUtil.extractEmail(token);
    }

    public User getUserFromToken(String authHeader) throws IllegalArgumentException {
        String email = getEmailFromToken(authHeader);
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user;
    }

    public ResponseEntity<User> validateAndGetUser(String authHeader) {
        try {
            User user = getUserFromToken(authHeader);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }
}
