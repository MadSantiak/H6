package org.psyche.assistant.Controller

import org.psyche.assistant.Model.Group.Group
import org.psyche.assistant.Model.User.User
import org.psyche.assistant.Service.GroupService

class GroupController {
    private val groupService = GroupService()

    suspend fun getGroupDetails(id: Int): Group? {
        return groupService.getGroupDetails(id)
    }

    suspend fun createGroup(authToken: String): Group? {
        return groupService.createGroup(authToken)
    }

    suspend fun joinGroup() {}
}