package org.psyche.assistant.Service

import org.psyche.assistant.appContext
import java.io.InputStream
import java.util.*

actual object ConfigProvider {
    private val properties: Properties = Properties().apply {
        val inputStream: InputStream = appContext.assets.open("service.properties")
        load(inputStream)
    }

    actual val baseUrl: String by lazy {
        properties.getProperty("BASE_URL")
    }
    actual val secondaryUrl: String? by lazy {
        properties.getProperty("SECONDARY_URL")
    }
    actual val tertiaryUrl: String? by lazy {
        properties.getProperty("TERTIARY_URL")
    }
}