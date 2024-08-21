package org.psyche.assistant.Service

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.datetime.LocalDate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.psyche.assistant.Model.Activity.Activity
import org.psyche.assistant.Model.Activity.ActivityRepository

/**
 * Activity service
 * Makes use of the ActivityRepository interface to ensure functionality is present.
 * @constructor Create empty Activity service
 */
class ActivityService : ActivityRepository {
    private val client = HttpClient()

    /**
     * Get handled activity by period
     * Note that this looks specifically at when activities were handled; not their deadline.
     * @param groupId
     * @param startDate
     * @param endDate
     * @return
     */
    override suspend fun getHandledActivityByPeriod(groupId: Int, startDate: LocalDate, endDate: LocalDate): List<Activity> {
        val url = ServiceBuilder.url("api/activities/group/$groupId/handled/period?startDate=${startDate}&endDate=${endDate}")
        val response: HttpResponse = client.get(url) {
            contentType(ContentType.Application.Json)
        }

        return when (response.status) {
            HttpStatusCode.OK -> {
                val responseBody = response.bodyAsText()
                Json.decodeFromString<List<Activity>>(responseBody)
            }
            else -> emptyList()
        }
    }

    /**
     * Get activity by period
     * Note this looks on the activities deadline.
     * @param groupId
     * @param startDate
     * @param endDate
     * @return
     */
    override suspend fun getActivityByPeriod(groupId: Int, startDate: LocalDate, endDate: LocalDate): List<Activity> {
        val url = ServiceBuilder.url("api/activities/group/$groupId/period?startDate=${startDate}&endDate=${endDate}")
        val response: HttpResponse = client.get(url) {
            contentType(ContentType.Application.Json)
        }

        return when (response.status) {
            HttpStatusCode.OK -> {
                val responseBody = response.bodyAsText()
                Json.decodeFromString<List<Activity>>(responseBody)
            }
            else -> emptyList()
        }
    }

    /**
     * Get activity for today
     * Gets only activities relevant for today' date.
     * @param groupId
     * @param today
     * @return
     */
    override suspend fun getActivityForToday(groupId: Int, today: LocalDate): List<Activity> {
        val url = ServiceBuilder.url("api/activities/group/$groupId?date=${today}")
        val response: HttpResponse = client.get(url) {
            contentType(ContentType.Application.Json)
        }

        return when (response.status) {
            HttpStatusCode.OK -> {
                val responseBody = response.bodyAsText()
                val activities = Json.decodeFromString<List<Activity>>(responseBody)

                activities.filter { activity ->
                    activity.deadline == today
                }
            }
            else -> emptyList()
        }
    }

    /**
     * Get activity
     * Fetches an activity from API based on the passed ID
     * @param activityId
     * @return
     */
    override suspend fun getActivity(activityId: Int): Activity?{
        val url = ServiceBuilder.url("api/activities/$activityId")
        val response: HttpResponse = client.get(url) {
            contentType(ContentType.Application.Json)
        }

        return when (response.status) {
            HttpStatusCode.OK -> {
                val responseBody = response.bodyAsText()
                Json.decodeFromString<Activity>(responseBody)
            }
            else -> null
        }
    }

    /**
     * Create activity
     * Creates an activity so long as authToken is valid.
     * @param authToken
     * @param deadline
     * @param description
     * @param energyCost
     * @return
     */
    override suspend fun createActivity(authToken: String, deadline: String, description: String, energyCost: Int): Activity? {
        val url = ServiceBuilder.url("api/activities/create")

        val requestPayload = Json.encodeToString(mapOf(
            "deadline" to deadline,
            "description" to description,
            "energyCost" to energyCost.toString()
        ))

        val response: HttpResponse = client.post(url) {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, "Bearer $authToken")
            setBody(requestPayload)
        }

        return when (response.status) {
            HttpStatusCode.Created -> {
                val responseBody = response.bodyAsText()
                Json.decodeFromString<Activity>(responseBody)
            }
            else -> null
        }
    }

    /**
     * Complete activity
     * API call to complete activities, requires valid authToken.
     * @param authToken
     * @param activityId
     * @return
     */
    override suspend fun completeActivity(authToken: String, activityId: Int): Activity? {
        val url = ServiceBuilder.url("api/activities/$activityId/complete")
        val response: HttpResponse = client.post(url) {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, "Bearer $authToken")
        }

        return when (response.status) {
            HttpStatusCode.OK -> {
                val responseBody = response.bodyAsText()
                Json.decodeFromString<Activity>(responseBody)
            }
            else -> null
        }
    }

    /**
     * Delete activity
     * Deletes activity based solely on ID.
     * @param activityId
     * @return
     */
    override suspend fun deleteActivity(authToken: String, activityId: Int): Boolean {
        val url = ServiceBuilder.url("api/activities/$activityId/delete")
        val response: HttpResponse = client.delete(url) {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, "Bearer $authToken")
        }

        return when (response.status) {
            HttpStatusCode.NoContent -> true
            HttpStatusCode.NotFound -> false
            else -> throw Exception(response.status.toString())
        }
    }
}