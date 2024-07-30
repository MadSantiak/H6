package org.psyche.assistant

/**
 * Expect/Actual structure for loading translations contained in JSON files.
 * Note that for more generalized UI translations, xml/StringResource patterns are used.
 */
expect object JsonTranslationLoader {
    fun loadTranslations(locale: String)
    fun getTranslationString(key: String): String
}