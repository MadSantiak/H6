package org.psyche.assistant.Model.Group

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Group (
    val id: Int,

    // Clarify field name since back-end operates with objects, and front-end with IDs
    @SerialName("users")
    val userIds: List<Int>,
    @SerialName("activities")
    val activityIds: List<Int>,

    @SerialName("owner")
    val ownerId: Int?,

    val code: String,
)