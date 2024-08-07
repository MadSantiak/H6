package org.psyche.assistant.Controller

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.psyche.assistant.Model.User.User
import org.psyche.assistant.Model.User.UserRepository

class UserController : UserRepository {

    private val client = ServiceBuilder.client

    override suspend fun getUserId(id: Int): User {
        val url = ServiceBuilder.url("users/$id")
        return client.get(url).body()
    }

    override suspend fun addUser(user: User): Int {
        val url = ServiceBuilder.url("users")
        val response = client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(user)
        }
        if (response.status == HttpStatusCode.Created) {
            val createdUser: User = response.body()
            return createdUser.id
        } else {
            throw Exception("Failed to add user: ${response.status}")
        }
    }
}
