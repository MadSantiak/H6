package com.example.PsycheAssistantAPI.Repository;

import com.example.PsycheAssistantAPI.Model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Note that JPA repository implements basic CRUD functionality.
 * It is also able to automatically generate simple queries based on naming conventions
 * employed in the function definition.
 */
public interface GroupRepository extends JpaRepository<Group, Integer> {
    Group findByCode(String code);
}
