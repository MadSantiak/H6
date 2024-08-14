package org.psyche.assistant.Storage

import com.tencent.mmkv.MMKV

actual object AuthStorage {
    private const val AUTH_TOKEN_KEY = "auth_token"
    private const val REFRESH_TOKEN_KEY = "refresh_token"

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