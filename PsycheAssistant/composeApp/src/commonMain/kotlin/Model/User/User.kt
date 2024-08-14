package org.psyche.assistant.Model.User

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User (
    val id: Int,
    val email: String,
    val password: String,
    val energyExpenditure: Int,

    // Clarify field name since back-end operates with objects, and front-end with IDs
    @SerialName("group")
    val groupId: Int? = null,
)