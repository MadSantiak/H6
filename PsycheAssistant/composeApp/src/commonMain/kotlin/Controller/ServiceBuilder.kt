package org.psyche.assistant.Controller
// src/commonMain/kotlin/com/example/shared/ServiceBuilder.kt

import io.ktor.client.HttpClient
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object ServiceBuilder {
    private const val BASE_URL: String = "http://192.168.1.165:8080"
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    // Helper method to ensure controllers can be URL agnostic.
    fun url(path: String): String = "$BASE_URL/$path"
}
