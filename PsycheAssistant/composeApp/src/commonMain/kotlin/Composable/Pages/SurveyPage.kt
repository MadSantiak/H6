package org.psyche.assistant.Composable.Pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.psyche.assistant.Composable.Items.QuestionItem
import org.psyche.assistant.Composable.Sections.SurveyResultSection
import org.psyche.assistant.Helper.GlobalLocaleState
import org.psyche.assistant.JsonLoader
import org.psyche.assistant.Model.SurveyModel
import org.psyche.assistant.Model.SurveyRepository
import psycheassistant.composeapp.generated.resources.Res
import psycheassistant.composeapp.generated.resources.*
import roundToDecimals

/**
 * Composable SurveyPage, used as a frame to display each subsequent question via QuestionItem composable.
 * Note that JsonLoader uses JsonTranslatorLoader as an injected function to fetch translated JSON versions of the questions.
 */
@Composable
fun SurveyPage(onBack: () -> Unit) {

    val questions = remember { JsonLoader.loadQuestions(GlobalLocaleState.locale) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0.0)}

    var surveyModel = remember { SurveyModel(null, questions, null, 0.0, "")}

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        /**
         * Check whether all questions have been answered (iterated through),
         * calling QuestionItem composable to generate the view for each question
         */
        if (currentQuestionIndex < questions.size) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                QuestionItem(
                    questionModel = questions[currentQuestionIndex],
                    onNext = { value ->
                        questions[currentQuestionIndex].value = value
                        score += value
                        currentQuestionIndex++ },
                    onBack = { value ->
                        score -= value
                        questions[currentQuestionIndex].value = 0f
                        currentQuestionIndex-- },
                    onCancel = onBack,
                    onSubmit = {
                        surveyModel.totalScore = score
                        // Note: Need to increment here to ensure the Survey exists out of the final question in the list.
                        currentQuestionIndex++
                    },
                    questionModels = questions
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(stringResource(Res.string.sum_score, score.roundToDecimals(1)), style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(24.dp))
            }
        } else {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                questions.map{questionModel -> questionModel.surveyModel = surveyModel}
                var db = SurveyRepository()
                db.insertSurvey(surveyModel)
                SurveyResultSection(surveyModel)
            }
        }
    }

}
