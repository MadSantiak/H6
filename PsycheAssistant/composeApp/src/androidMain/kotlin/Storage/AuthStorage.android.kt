package org.psyche.assistant.Storage

import com.tencent.mmkv.MMKV
import org.psyche.assistant.Storage.AuthStorage.properties
import org.psyche.assistant.appContext
import java.io.InputStream
import java.util.*

/**
 * Auth storage
 * Actual implementation for storing tokens safely locally, using keys to encode said token.
 * Uses Memory-Mapped Key-Value for secure storage (as opposed to SharedPreferences)
 * @constructor Create empty Auth storage
 */
actual object AuthStorage {
    private val properties: Properties = Properties().apply {
        val inputStream: InputStream = appContext.assets.open("service.properties")
        load(inputStream)
    }

    val AUTH_TOKEN_KEY: String by lazy {
        properties.getProperty("auth_token")
    }
    val REFRESH_TOKEN_KEY: String by lazy {
        properties.getProperty("refresh_token")
    }

    actual fun saveAuthToken(token: String) {
        MMKV.defaultMMKV().encode(AUTH_TOKEN_KEY, token)
    }

    actual fun getAuthToken(): String? {
        return MMKV.defaultMMKV().decodeString(AUTH_TOKEN_KEY)
    }

    actual fun removeAuthToken() {
        MMKV.defaultMMKV().remove(AUTH_TOKEN_KEY)
    }

    actual fun saveRefreshToken(token: String) {
        MMKV.defaultMMKV().encode(REFRESH_TOKEN_KEY, token)
    }

    actual fun getRefreshToken(): String? {
        return MMKV.defaultMMKV().decodeString(REFRESH_TOKEN_KEY)
    }

    actual fun removeRefreshToken() {
        MMKV.defaultMMKV().remove(REFRESH_TOKEN_KEY)
    }
}