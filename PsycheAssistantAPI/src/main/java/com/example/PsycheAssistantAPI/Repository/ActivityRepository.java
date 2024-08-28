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

/**
 * Note JPA implements many of the most basic CRUD functions for communicating with the database.
 * More advanced queries cna be defined individually, as seen below.
 */
public interface ActivityRepository extends JpaRepository<Activity, Integer> {

    /**
     * Gets activities for the group based on the activitity's deadline
     * @param groupId
     * @param date
     * @return
     */
    @Query("SELECT a FROM Activity a WHERE a.group.id = :groupId AND a.deadline = :date")
    List<Activity> findActivitiesForGroupWithDeadline(
            @Param("groupId") int groupId,
            @Param("date") LocalDate date);

    /**
     * Gets activities for a group where the deadline falls between two specific dates.
     * @param groupId
     * @param startDate
     * @param endDate
     * @return
     */
    @Query("SELECT a FROM Activity a WHERE a.group.id = :groupId AND a.deadline BETWEEN :endDate AND :startDate")
    List<Activity> findActivitiesForPeriod(
            @Param("groupId") int groupId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * Gets activities from a group where they were handled (i.e. completed) between two specific dates.
     * @param groupId
     * @param startDate
     * @param endDate
     * @return
     */
    @Query("SELECT a FROM Activity a WHERE a.group.id = :groupId AND a.handledOn BETWEEN :endDate AND :startDate")
    List<Activity> findHandledActivitiesForPeriod(
            @Param("groupId") int groupId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * Attempts to null user-specific values on activities handled by a given user, i.e. when that user is deleted.
     * Transactional, so if one function fails, the entire function tree fails.
     * @param user
     */
    @Modifying
    @Transactional
    @Query("UPDATE Activity a SET a.handledBy = NULL, a.handledOn = NULL, a.completed = FALSE WHERE a.handledBy = :user")
    void updateActivitiesHandledByUser(@Param("user") User user);

    /**
     * Transactional function that attempts to remove the completed state and owner of an activity.
     * @param user
     */
    @Modifying
    @Transactional
    @Query("UPDATE Activity a SET a.owner = NULL, a.completed = FALSE WHERE a.owner = :user")
    void updateActivitiesOwnedByUser(@Param("user") User user);
}

