package org.psyche.assistant.Model.User

import kotlinx.serialization.Serializable
import org.psyche.assistant.Model.Group.Group

@Serializable
data class User (
    val id: Int,
    val email: String,
    val password: String,
    val energyExpenditure: Int,
    val group: Group? = null,
)