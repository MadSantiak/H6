package com.example.PsycheAssistantAPI.Repository;

import com.example.PsycheAssistantAPI.Model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {
}
