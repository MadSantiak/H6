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
        val response: HttpResponse = client.get(url) {
            header(HttpHeaders.Authorization, "Bearer $authToken")
        }
        return when (response.status) {
            HttpStatusCode.OK -> {
                val responseBody = response.bodyAsText()
                Json.decodeFromString<User>(responseBody)
            }
            else -> null
        }
    }

    override suspend fun registerUser(email: String, password: String): String {
        val url = ServiceBuilder.url("api/users/register")
        val response: HttpResponse = client.post(url) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody("email=$email&password=$password")
        }
        return when (response.status) {
            HttpStatusCode.Created -> response.bodyAsText()
            else -> throw Exception("Failed to register user: ${response.status}")
        }
    }

    override suspend fun loginUser(email: String, password: String): String {
        val url = ServiceBuilder.url("api/auth/login")
        val response: HttpResponse = client.post(url) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody("email=$email&password=$password")
        }
        if (response.status == HttpStatusCode.OK) {
            val token = response.bodyAsText()
            AuthStorage.saveAuthToken(token) // Save the token
            return token
        } else {
            throw Exception("Failed to login user: ${response.status}")
        }
    }

    override suspend fun logoutUser() {
        AuthStorage.removeAuthToken()
    }
}
