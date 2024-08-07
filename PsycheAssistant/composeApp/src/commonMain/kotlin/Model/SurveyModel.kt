package org.psyche.assistant.Model

import kotlinx.serialization.Serializable
import org.psyche.assistant.Model.User.User


/**
 * Stores the entire Survey after answering and submitting all the questions via SurveyPage.
 */
@Serializable
class SurveyModel(
    val id: Int?,
    val questionModels: List<QuestionModel>?,
    val user: User?,
    var totalScore: Double,
    val date: String?
)