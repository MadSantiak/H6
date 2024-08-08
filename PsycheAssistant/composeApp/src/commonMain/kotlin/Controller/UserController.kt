package org.psyche.assistant.Controller

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.psyche.assistant.Model.User.User
import org.psyche.assistant.Storage.AuthStorage

class UserController {

    private val client = ServiceBuilder.client

    suspend fun registerUser(email: String, password: String): String {
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

    suspend fun loginUser(email: String, password: String): String {
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
}
