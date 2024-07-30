package org.psyche.assistant.Model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.psyche.assistant.Helper.create

/**
 * Repository for how to communicate with the database to allow for CRUD of SurveyModels/Survey database models.
 */
class SurveyRepository {
    private val db = create()
    private val queries = db.psycheAssistantQueries

    fun insertSurvey(surveyModel: SurveyModel) {
        val date: Instant = Clock.System.now()
        queries.insertSurvey(date.toString(), surveyModel.totalScore.toDouble())
    }

    fun selectAllSurveys() = queries.selectAllSurveys().executeAsList()

    fun deleteSurveyById(surveyId: Long) {
        queries.deleteSurvey(surveyId)
    }
}
