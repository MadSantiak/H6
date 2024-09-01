package org.psyche.assistant.Service

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

object ServiceBuilder {
    private val BASE_URL = ConfigProvider.baseUrl
    private val SECONDARY_URL = ConfigProvider.secondaryUrl
    private val TERTIARY_URL = ConfigProvider.tertiaryUrl

    private val activeBaseUrl: String by lazy {
        determineActiveBaseUrl() ?: throw IllegalStateException("Unable to establish a connection to any server..")
    }


    private val client = HttpClient(CIO) {
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
    fun url(path: String): String = "$activeBaseUrl/$path"

    /**
     * Attempts to reach a general, unauthenticated endpoint to set the active base URL.
     */
    private fun determineActiveBaseUrl(): String? {
        val urls = listOfNotNull(BASE_URL, SECONDARY_URL, TERTIARY_URL)

        for (url in urls) {
            if (isUrlReachable("$url/api/users")) {
                return url
            }
        }
        return null
    }

    /**
     * Simple check to verify if the URL is reachable.
     */
    private fun isUrlReachable(url: String): Boolean = runBlocking {
        try {
            val response = client.get(url) {
                method = HttpMethod.Get
            }
            response.status.isSuccess()
        } catch (e: Exception) {
            false
        }
    }
}
