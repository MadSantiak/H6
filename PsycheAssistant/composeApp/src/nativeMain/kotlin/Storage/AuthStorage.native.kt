package org.psyche.assistant.Storage

actual object AuthStorage {
    actual fun saveAuthToken(token: String) {
    }

    actual fun getAuthToken(): String? {
        TODO("Not yet implemented")
    }

    actual fun removeAuthToken() {
    }
}