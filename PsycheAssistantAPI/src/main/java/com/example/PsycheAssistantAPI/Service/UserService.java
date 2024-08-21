package com.example.PsycheAssistantAPI.Service;

import com.example.PsycheAssistantAPI.Model.Activity;
import com.example.PsycheAssistantAPI.Model.Group;
import com.example.PsycheAssistantAPI.Model.User;
import com.example.PsycheAssistantAPI.Repository.ActivityRepository;
import com.example.PsycheAssistantAPI.Repository.GroupRepository;
import com.example.PsycheAssistantAPI.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private GroupRepository groupRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User findById(int id) { return userRepository.findById(id).orElse(null); }

    public List<User> findAll() { return userRepository.findAll(); }

    public void registerUser(String email, String password) {
        if (userRepository.findByEmail(email) != null) {
            throw new RuntimeException("User already exists");
        }
        if (!isValidEmail(email)) {
            throw new RuntimeException("Invalid email format");
        }
        if (!isValidPassword(password)) {
            throw new RuntimeException("Password does not meet complexity requirements");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public boolean deleteUser(User user) {
        activityRepository.updateActivitiesHandledByUser(user);
        activityRepository.updateActivitiesOwnedByUser(user);
        Group group = user.getGroup();
        group.removeUser(user);
        if (user == group.getOwner()) {
            group.setOwner(null);
        }
        userRepository.delete(user);
        return true;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.chars().anyMatch(Character::isDigit) && password.chars().anyMatch(Character::isLetter);
    }
}
