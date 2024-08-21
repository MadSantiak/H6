package org.psyche.assistant.Controller

import kotlinx.datetime.LocalDate
import org.psyche.assistant.Model.Activity.Activity
import org.psyche.assistant.Service.ActivityService

class ActivityController {
    private val activityService = ActivityService()

    suspend fun getHandledActivityByPeriod(groupId: Int, startDate: LocalDate, endDate: LocalDate): List<Activity> {
        return activityService.getHandledActivityByPeriod(groupId, startDate, endDate)
    }

    suspend fun getActivityByPeriod(groupId: Int, startDate: LocalDate, endDate: LocalDate): List<Activity> {
        return activityService.getActivityByPeriod(groupId, startDate, endDate)
    }

    suspend fun getActivityForToday(groupId: Int, today: LocalDate): List<Activity> {
        return activityService.getActivityForToday(groupId, today)
    }

    suspend fun createActivity(authToken: String, deadline: String, description: String, energyCost: Int): Activity? {
        return activityService.createActivity(authToken, deadline, description, energyCost)
    }

    suspend fun completeActivity(authToken: String, activityId: Int): Activity? {
        return activityService.completeActivity(authToken, activityId)
    }

    suspend fun deleteActivity(authToken: String, activityId: Int): Boolean {
        return activityService.deleteActivity(authToken, activityId)
    }

}