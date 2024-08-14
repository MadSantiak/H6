package org.psyche.assistant.Service

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.psyche.assistant.Model.Group.Group
import org.psyche.assistant.Model.Group.GroupRepository

class GroupService : GroupRepository {
    private val client = HttpClient()


    override suspend fun createGroup(authToken: String): Group? {
        val url = ServiceBuilder.url("api/groups/register")
        val response: HttpResponse = client.post(url) {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, "Bearer $authToken")
        }
        return when (response.status) {
            HttpStatusCode.Created -> {
                val responseBody = response.bodyAsText()
                Json.decodeFromString<Group>(responseBody)
            }
            else -> null
        }
    }

    override suspend fun joinGroup(authToken: String, code: String): Group? {
        val url = ServiceBuilder.url("api/groups/join")
        val response: HttpResponse = client.post(url) {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, "Bearer $authToken")
            setBody(Json.encodeToString(mapOf("code" to code)))
        }
        return when (response.status) {

            HttpStatusCode.OK -> {
                val responseBody = response.bodyAsText()
                Json.decodeFromString<Group>(responseBody)
            }
            else -> null
        }
    }
    override suspend fun leaveGroup() {
        TODO("Not yet implemented")
    }

    override suspend fun getGroupDetails(id: Int): Group? {
        val url = ServiceBuilder.url("api/groups/$id")
        val response: HttpResponse = client.get(url) {
            contentType(ContentType.Application.Json)
        }

        return when (response.status) {
            HttpStatusCode.OK -> {
                val responseBody = response.bodyAsText()
                Json.decodeFromString<Group>(responseBody)
            }
            else -> null
        }
    }

    override suspend fun removeUserFromGroup(authToken: String, groupId: Int, userId: Int): Group? {
        val url = ServiceBuilder.url("api/groups/$groupId/kick")
        val response: HttpResponse = client.post(url) {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, "Bearer $authToken")
            setBody(Json.encodeToString(mapOf("userId" to userId)))
        }

        return when (response.status) {
            HttpStatusCode.OK -> {
                val responseBody = response.bodyAsText()
                Json.decodeFromString<Group>(responseBody)
            }
            else -> null
        }
    }


}