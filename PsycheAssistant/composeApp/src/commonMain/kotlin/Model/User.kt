package org.psyche.assistant.Model

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val id: Int,
    val name: String,
) {
}