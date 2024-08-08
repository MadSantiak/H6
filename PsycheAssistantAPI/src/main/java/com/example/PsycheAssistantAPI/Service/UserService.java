package com.example.PsycheAssistantAPI.Service;

import com.example.PsycheAssistantAPI.Model.User;
import com.example.PsycheAssistantAPI.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void registerUser(String email, String password) {
        if (userRepository.findByEmail(email) != null) {
            throw new RuntimeException("User already exists");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
