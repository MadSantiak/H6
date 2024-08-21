package org.psyche.assistant.Model.User


interface UserRepository {
    suspend fun getUserDetails(authToken: String): User?
    suspend fun registerUser(email: String, password: String): String
    suspend fun loginUser(email: String, password: String): String
    suspend fun logoutUser()
    suspend fun deleteUser(authToken: String): Boolean
    suspend fun getUserById(id: Int): User?
}