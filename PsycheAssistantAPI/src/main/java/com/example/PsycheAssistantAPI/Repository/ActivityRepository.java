package com.example.PsycheAssistantAPI.Repository;

import com.example.PsycheAssistantAPI.Model.Activity;
import com.example.PsycheAssistantAPI.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {
    @Query("SELECT a FROM Activity a WHERE a.group.id = :groupId AND a.deadline = :date")
    List<Activity> findActivitiesForGroupWithDeadline(
            @Param("groupId") int groupId,
            @Param("date") LocalDate date);

    @Query("SELECT a FROM Activity a WHERE a.group.id = :groupId AND a.deadline BETWEEN :endDate AND :startDate")
    List<Activity> findActivitiesForPeriod(
            @Param("groupId") int groupId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT a FROM Activity a WHERE a.group.id = :groupId AND a.handledOn BETWEEN :endDate AND :startDate")
    List<Activity> findHandledActivitiesForPeriod(
            @Param("groupId") int groupId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Modifying
    @Transactional
    @Query("UPDATE Activity a SET a.handledBy = NULL, a.handledOn = NULL, a.completed = FALSE WHERE a.handledBy = :user")
    void updateActivitiesHandledByUser(@Param("user") User user);

    @Modifying
    @Transactional
    @Query("UPDATE Activity a SET a.owner = NULL, a.completed = FALSE WHERE a.owner = :user")
    void updateActivitiesOwnedByUser(@Param("user") User user);
}

