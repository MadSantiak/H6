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

class ActivityService : ActivityRepository {
    private val client = HttpClient()

    override suspend fun getTodayActivityForGroup(groupId: Int, today: LocalDate): List<Activity> {
        val url = ServiceBuilder.url("api/activities/group/$groupId?date=${today.toString()}")
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

    override suspend fun completeActivity(activityId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteActivity(activityId: Int) {
        TODO("Not yet implemented")
    }
}