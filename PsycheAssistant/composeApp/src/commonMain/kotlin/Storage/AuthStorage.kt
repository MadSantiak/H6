package org.psyche.assistant.Storage

expect object AuthStorage {
    fun saveAuthToken(token: String)
    fun getAuthToken(): String?
    fun removeAuthToken()
    fun saveRefreshToken(token: String)
    fun getRefreshToken(): String?
    fun removeRefreshToken()
}