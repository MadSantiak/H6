package com.example.PsycheAssistantAPI.Repository;

import com.example.PsycheAssistantAPI.Model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {
    @Query("SELECT a FROM Activity a WHERE a.group.id = :groupId AND a.deadline = :date")
    List<Activity> findActivitiesForGroupWithDeadline(
            @Param("groupId") int groupId,
            @Param("date") LocalDate date);
}
