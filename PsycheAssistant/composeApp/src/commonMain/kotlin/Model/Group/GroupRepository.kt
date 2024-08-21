package org.psyche.assistant.Model.Group

interface GroupRepository {
    suspend fun createGroup(authToken: String): Group?
    suspend fun joinGroup(authToken: String, code: String): Group?
    suspend fun leaveGroup(authToken: String, groupId: Int): Boolean
    suspend fun disbandGroup(authToken: String, groupId: Int): Boolean
    suspend fun getGroupDetails(id: Int): Group?
    suspend fun removeUserFromGroup(authToken: String, groupId: Int, userId: Int): Group?
}