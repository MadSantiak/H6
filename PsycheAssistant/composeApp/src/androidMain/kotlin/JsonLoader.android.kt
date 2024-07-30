package org.psyche.assistant

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.psyche.assistant.Model.QuestionModel

/**
 * The actual function used (for Android), returning the questions stored in JSON as Question objects via serialization.
 * Note that "appContext" is a global variable defined in MainActivity,
 * Also note that we fetch the selected internationlization (i18n) JSON version, via the TranslationLoader.
 */
actual object JsonLoader {
    actual fun loadQuestions(locale: String): List<QuestionModel> {
        JsonTranslationLoader.loadTranslations(locale)

        val inputStream = appContext.assets.open("survey_questions.json")

        val jsonString = inputStream.bufferedReader().use { it.readText() }

        val questionModels = Json.decodeFromString<List<QuestionModel>>(jsonString)

        return questionModels.map {
            it.copy(question = JsonTranslationLoader.getTranslationString("question_${it.id}"))
        }
    }
}