package org.psyche.assistant.Model.Activity

import kotlinx.datetime.LocalDate

interface ActivityRepository {
    suspend fun getHandledActivityByPeriod(groupId: Int, startDate: LocalDate, endDate: LocalDate): List<Activity>
    suspend fun getActivityByPeriod(groupId: Int, startDate: LocalDate, endDate: LocalDate): List<Activity>
    suspend fun getActivityForToday(groupId: Int, today: LocalDate): List<Activity>
    suspend fun getActivity(activityId: Int): Activity?
    suspend fun createActivity(authToken: String, deadline: String, description: String, energyCost: Int): Activity?
    suspend fun completeActivity(authToken: String, activityId: Int): Activity?
    suspend fun deleteActivity(authToken: String, activityId: Int): Boolean
}