package org.psyche.assistant.Model

import kotlinx.serialization.Serializable


/**
 * Serializable object, allowing us to make use of the JSON file for displaying questions.
 * This allows for offline functionality, but can still allow for updates, by for example updating the contents of the file.
 */
@Serializable
data class QuestionModel(
    val id: Int,
    val question: String,
    val type: String = "numeric",
    val options: List<String>? = null,
    var surveyModel: SurveyModel?,
    var value: Float = 0f,
)
