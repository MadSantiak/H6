package org.psyche.assistant.Model.Group

import kotlinx.serialization.Serializable
import org.psyche.assistant.Model.User.User

@Serializable
data class Group (
    val id: Int,
    val users: List<User>
)