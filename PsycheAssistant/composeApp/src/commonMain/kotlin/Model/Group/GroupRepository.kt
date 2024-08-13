package org.psyche.assistant.Model.Group

import org.psyche.assistant.Model.User.User

interface GroupRepository {
    suspend fun createGroup(authToken: String): Group?
    suspend fun joinGroup(authToken: String, code: String): Group?
    suspend fun leaveGroup()
    suspend fun getGroupDetails(id: Int): Group?
    suspend fun removeUserFromGroup(authToken: String, groupId: Int, userId: Int): Group?
}