package org.psyche.assistant.Controller

import kotlinx.datetime.LocalDate
import org.psyche.assistant.Model.Activity.Activity
import org.psyche.assistant.Model.Group.Group
import org.psyche.assistant.Service.ActivityService
import org.psyche.assistant.Service.GroupService

class ActivityController {
    private val activityService = ActivityService()

    suspend fun getTodayActivityForGroup(groupId: Int, today: LocalDate): List<Activity> {
        return activityService.getTodayActivityForGroup(groupId, today)
    }

    suspend fun createActivity(authToken: String, deadline: String, description: String, energyCost: Int): Activity? {
        return activityService.createActivity(authToken, deadline, description, energyCost)
    }

    suspend fun completeActivity(activityId: Int) {
        return activityService.completeActivity(activityId)
    }

    suspend fun deleteActivity(activityId: Int) {
        return activityService.deleteActivity(activityId)
    }

}