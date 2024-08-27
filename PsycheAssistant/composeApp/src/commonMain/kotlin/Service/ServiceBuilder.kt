package org.psyche.assistant.Service

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object ServiceBuilder {
    // Moved url to separate file, albeit it not necessitates platform specific retrieval.
    private val BASE_URL = ConfigProvider.baseUrl

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    /**
     * Helper method to ensure controllers can be URL agnostic.
     */
    fun url(path: String): String = "$BASE_URL/$path"
}
