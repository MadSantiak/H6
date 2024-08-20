package org.psyche.assistant.Service

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import org.psyche.assistant.Model.User.User
import org.psyche.assistant.Model.User.UserRepository
import org.psyche.assistant.Storage.AuthStorage

class UserService : UserRepository {
    private val client = HttpClient()

    override suspend fun getUserDetails(authToken: String): User? {
        val url = ServiceBuilder.url("api/users/me")
        var response: HttpResponse = client.get(url) {
            header(HttpHeaders.Authorization, "Bearer $authToken")
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            // Token might be expired, try to refresh it
            val newToken = refreshToken()
            if (newToken != null) {
                AuthStorage.saveAuthToken(newToken)
                response = client.get(url) {
                    header(HttpHeaders.Authorization, "Bearer $newToken")
                }
            } else {
                // Refresh token failed or is not available
                throw Exception("Token refresh failed, user needs to log in again")
            }
        }

        return when (response.status) {
            HttpStatusCode.OK -> {
                val responseBody = response.bodyAsText()
                Json.decodeFromString<User>(responseBody)
            }
            else -> null
        }
    }

    /**
     * Register the user using the supplied credentials. Verify that email and password live up to security standards, before posting to server.
     */
    override suspend fun registerUser(email: String, password: String): String {
        validateEmail(email)
        validatePassword(password)

        val url = ServiceBuilder.url("api/users/register")
        val response: HttpResponse = client.post(url) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody("email=$email&password=$password")
        }
        return when (response.status) {
            HttpStatusCode.Created -> response.bodyAsText()
            else -> throw Exception(response.bodyAsText())
        }
    }

    override suspend fun loginUser(email: String, password: String): String {
        val url = ServiceBuilder.url("api/auth/login")
        val response: HttpResponse = client.post(url) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody("email=$email&password=$password")
        }
        if (response.status == HttpStatusCode.OK) {
            val responseBody = response.bodyAsText()
            val tokens = Json.decodeFromString<Map<String, String>>(responseBody)
            val accessToken = tokens["accessToken"]
            val refreshToken = tokens["refreshToken"]

            if (accessToken != null && refreshToken != null) {
                AuthStorage.saveAuthToken(accessToken)
                AuthStorage.saveRefreshToken(refreshToken)
                return accessToken
            } else {
                throw Exception("Invalid login response")
            }
        } else {
            throw Exception(response.bodyAsText())
        }
    }

    override suspend fun logoutUser() {
        AuthStorage.removeAuthToken()
        AuthStorage.removeRefreshToken()
    }

    override suspend fun getUserById(id: Int): User? {
        val url = ServiceBuilder.url("api/users/$id")
        val response: HttpResponse = client.get(url) {
            contentType(ContentType.Application.Json)
        }
        return when (response.status) {
            HttpStatusCode.OK -> {
                val responseBody = response.bodyAsText()
                Json.decodeFromString<User>(responseBody)
            }
            else -> null
        }
    }

    private suspend fun refreshToken(): String? {
        val refreshToken = AuthStorage.getRefreshToken() ?: return null

        val url = ServiceBuilder.url("api/auth/refresh-token")
        val response: HttpResponse = client.post(url) {
            header(HttpHeaders.Authorization, "Bearer $refreshToken")
        }

        return if (response.status == HttpStatusCode.OK) {
            val newAccessToken = response.bodyAsText() // Assuming the server returns a new access token
            newAccessToken
        } else {
            // Handle refresh token failure
            AuthStorage.removeAuthToken()
            AuthStorage.removeRefreshToken()
            null
        }
    }

    private fun validateEmail(email: String) {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        if (!email.matches(emailRegex.toRegex())) {
            throw RuntimeException("Invalid email format")
        }
    }

    private fun validatePassword(password: String) {
        if (password.length < 8 ||
            !password.any { it.isUpperCase() } ||
            !password.any { it.isLowerCase() } ||
            !password.any { it.isDigit() } ||
            !password.any { !it.isLetterOrDigit() }
        ) {
            throw RuntimeException("Password must be at least 8 characters long and include uppercase, lowercase, digit, and special character")
        }
    }
}
