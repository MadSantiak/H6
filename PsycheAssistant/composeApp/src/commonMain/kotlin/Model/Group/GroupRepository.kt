package org.psyche.assistant.Model.Group

import org.psyche.assistant.Model.User.User

interface GroupRepository {
    suspend fun createGroup(authToken: String): Group?
    suspend fun joinGroup(code: String)
    suspend fun leaveGroup()
    suspend fun getGroupDetails(id: Int): Group?
}