package org.psyche.assistant.Storage

/**
 * Auth storage
 * Expect object for platform specific implementation for storing tokens securely on the physical device itself.
 * See PsycheAssistant/composeApp/src/androidMain/kotlin/Storage/AuthStorage.android.kt
 * for Actual implementation.
 * @constructor Create empty Auth storage
 */
expect object AuthStorage {
    fun saveAuthToken(token: String)
    fun getAuthToken(): String?
    fun removeAuthToken()
    fun saveRefreshToken(token: String)
    fun getRefreshToken(): String?
    fun removeRefreshToken()
}