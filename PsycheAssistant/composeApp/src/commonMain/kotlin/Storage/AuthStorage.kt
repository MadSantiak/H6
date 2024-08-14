package org.psyche.assistant.Storage

expect object AuthStorage {
    fun saveAuthToken(token: String)
    fun getAuthToken(): String?
    fun removeAuthToken()
}