package org.psyche.assistant.Model.User

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val id: Int,
    val email: String,
    val password: String,
    val energyExpenditure: Int,
)