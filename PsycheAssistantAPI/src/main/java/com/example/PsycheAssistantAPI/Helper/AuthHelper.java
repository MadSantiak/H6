package com.example.PsycheAssistantAPI.Helper;

import com.example.PsycheAssistantAPI.Model.User;
import com.example.PsycheAssistantAPI.Service.UserService;
import com.example.PsycheAssistantAPI.Security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Helper class that takes care of interacting with jwtUtil to validate users
 */
@Component
public class AuthHelper {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthHelper(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    /**
     * Extracts the token from the authHeader received.
     * @param authHeader
     * @return
     * @throws IllegalArgumentException
     */
    public String extractToken(String authHeader) throws IllegalArgumentException {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid authorization header");
        }
        return authHeader.replace("Bearer ", "");
    }

    /**
     * Based on the token, fetches the email.
     * @param authHeader
     * @return
     * @throws IllegalArgumentException
     */
    public String getEmailFromToken(String authHeader) throws IllegalArgumentException {
        String token = extractToken(authHeader);
        return jwtUtil.extractEmail(token);
    }

    /**
     * Based on the fetched email, finds the specific user.
     * @param authHeader
     * @return
     * @throws IllegalArgumentException
     */
    public User getUserFromToken(String authHeader) throws IllegalArgumentException {
        String email = getEmailFromToken(authHeader);
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user;
    }

    /**
     * Fetches the user from the received authHeader, and returns it if found.
     * @param authHeader
     * @return
     */
    public ResponseEntity<User> validateAndGetUser(String authHeader) {
        try {
            User user = getUserFromToken(authHeader);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }
}
