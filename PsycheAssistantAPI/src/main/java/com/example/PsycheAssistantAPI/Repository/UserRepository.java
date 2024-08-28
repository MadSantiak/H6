package com.example.PsycheAssistantAPI.Repository;

import com.example.PsycheAssistantAPI.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Note that JPA repository implements basic CRUD functionality.
 * It is also able to automatically generate simple queries based on naming conventions
 * employed in the function definition.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

}
