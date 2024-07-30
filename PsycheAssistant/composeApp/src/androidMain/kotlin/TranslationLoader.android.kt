package org.psyche.assistant

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

actual object JsonTranslationLoader {

    var translations: Map<String, String> = emptyMap()

    actual fun loadTranslations(locale: String) {
        val inputStream = appContext.assets.open("i18n/$locale.json")
        val jsonContent = inputStream.bufferedReader().use { it.readText() }
        translations = Json.decodeFromString(jsonContent)
    }

    actual fun getTranslationString(key: String): String {
        return translations[key] ?: key
    }
}