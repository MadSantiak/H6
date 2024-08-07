package org.psyche.assistant.Model.User

interface UserRepository {
    suspend fun getUserId(id: Int): User
    suspend fun addUser(user: User): Int
}