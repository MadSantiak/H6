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

    /**
     * Create group
     * Creates a group via API, given a valid authToken is supplied.
     * @param authToken
     * @return
     */
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

    /**
     * Join group
     * Joins a group designated by the groups 6-character code (viewable by members of that group)
     * so long as there is also a valid authToken.
     * @param authToken
     * @param code
     * @return
     */
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

    /**
     * Leave group
     * Removes the user from the group, validation of ownership, relations etc., is handled by backend service.
     * @param authToken
     * @param groupId
     * @return
     */
    override suspend fun leaveGroup(authToken: String, groupId: Int): Boolean {
        val url = ServiceBuilder.url("api/groups/${groupId}/leave")
        val response: HttpResponse = client.post(url) {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, "Bearer $authToken")
        }

        return when (response.status) {
            HttpStatusCode.NoContent -> true
            HttpStatusCode.NotFound -> false
            else -> throw Exception(response.status.toString())
        }
    }

    /**
     * Disband group
     * Deletes the group and its relations to members and activities; i.e. nullifies the relation - orphans remain.
     * @param authToken
     * @param groupId
     * @return
     */
    override suspend fun disbandGroup(authToken: String, groupId: Int): Boolean {
        val url = ServiceBuilder.url("api/groups/${groupId}/disband")
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

    /**
     * Get group details
     * General purpose read function for specific group. Note that no personal or critical data is stored, therefore no authentication is required.
     * @param id
     * @return
     */
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

    /**
     * Remove user from group
     * Removes the user in question from the group. Note that this can be used either to kick or voluntarily leave the group
     * included direct reference from unimplemented version to clarify this.
     * @param authToken
     * @param groupId
     * @param userId
     * @return
     */
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