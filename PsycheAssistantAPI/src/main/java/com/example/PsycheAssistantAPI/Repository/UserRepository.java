package com.example.PsycheAssistantAPI.Repository;

import com.example.PsycheAssistantAPI.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

}
