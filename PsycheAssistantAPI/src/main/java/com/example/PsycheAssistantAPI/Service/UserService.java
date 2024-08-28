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

/**
 * Links Controller (end-points) to Repository (database)
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private GroupRepository groupRepository;

    /**
     * Initialize BCryptPasswordEncoder, used to store user passwords safely hashed. Note it internally applies salt as well.
     */
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User findById(int id) { return userRepository.findById(id).orElse(null); }

    public List<User> findAll() { return userRepository.findAll(); }

    /**
     * Register the user with the supplied email and (hashed) password.
     * @param email
     * @param password
     */
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

    /**
     * Finds the user based on the email.
     * @param email
     * @return
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Attempts to delete the user after nulling activities they have handled, and removing them from their group.
     * @param user
     * @return
     */
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

    /**
     * Verifies the supplied email is valid, i.e. that there is:
     * a local part containing any number of characters a-zA-Z0-9_ and -,
     * separating @
     * domain (and subdomain) part of any number of characters a-zA-Z
     * a separating .
     * a top-level domain between 2 - 4 characters in length
     * @param email
     * @return
     */
    private boolean isValidEmail(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    /**
     * Checks the validity of the password, i.e. lenght, contains digit, contains letters.
     * Stricter format validity is handled in front-end.
     * @param password
     * @return
     */
    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.chars().anyMatch(Character::isDigit) && password.chars().anyMatch(Character::isLetter);
    }
}
