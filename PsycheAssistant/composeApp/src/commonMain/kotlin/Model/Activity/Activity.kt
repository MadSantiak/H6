package org.psyche.assistant.Model.Activity

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Activity (
    val id: Int,
    val description: String,
    val energyCost: Int,

    // Clarify field name since back-end operates with objects, and front-end with IDs
    @SerialName("group")
    val groupId: Int? = null,

    @SerialName("owner")
    val ownerId: Int? = null,

    @SerialName("handledBy")
    val handledById: Int? = null,

    val deadline: LocalDate,
    val handledOn: LocalDate? = null,
    val completed: Boolean = false
)