package org.psyche.assistant.Model.Activity

import kotlinx.datetime.LocalDate
import org.psyche.assistant.Model.Group.Group

interface ActivityRepository {
    suspend fun getTodayActivityForGroup(groupId: Int, today: LocalDate): List<Activity>
    suspend fun getActivity(activityId: Int): Activity?
    suspend fun createActivity(authToken: String, deadline: String, description: String, energyCost: Int): Activity?
    suspend fun completeActivity(activityId: Int)
    suspend fun deleteActivity(activityId: Int)
}