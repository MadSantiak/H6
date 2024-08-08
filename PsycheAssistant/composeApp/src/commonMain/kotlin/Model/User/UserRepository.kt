package org.psyche.assistant.Model.User

interface UserRepository {
    suspend fun registerUser(email: String, password: String): String
    suspend fun loginUser(email: String, password: String): String
}