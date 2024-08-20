package org.psyche.assistant.Composable.Sections

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.psyche.assistant.Model.SurveyModel
import psycheassistant.composeapp.generated.resources.Res
import psycheassistant.composeapp.generated.resources.*
import roundToDecimals
import kotlin.math.roundToInt
import org.psyche.assistant.Helper.LevelColor

/**
 * ResultView used for displaying a summary of:
 * - The score (color coded via scoreColor)
 * - The fatigue level equivalent to that score (LevelIndicator)
 * - The general symptoms relevant for that level of fatigue (getFatigueSymptoms)
 * - The general recommendations for that level of fatigue (getFatigueRecommendations)
 * on the SurveyPage.
 */
@Composable
fun SurveyResultSection(surveyModel: SurveyModel) {

    val scoreColor = LevelColor.getColor(surveyModel.totalScore)


    // Score summary
    Text(stringResource(Res.string.completed), style = MaterialTheme.typography.h5)
    Text(stringResource(Res.string.final_score, surveyModel.totalScore.roundToDecimals(1)), color = scoreColor)
    Spacer(modifier = Modifier.height(24.dp))
    LevelIndicator(totalScore = surveyModel.totalScore)

    // Symptoms summary
    Text(stringResource(Res.string.symptoms), style = MaterialTheme.typography.h6)
    Text(getFatigueSymptoms(surveyModel.totalScore))
    Spacer(modifier = Modifier.height(24.dp))

    // Recommendations summary
    Text(stringResource(Res.string.recommendations), style = MaterialTheme.typography.h6)
    Text(getFatigueRecommendations(surveyModel.totalScore))
}

/**
 * Composable function that retrieves string resource for symptoms based on the Double (survey.totalScore) passed to it.
 */
@Composable
fun getFatigueSymptoms(score: Double): String {
    return when {
        score <= 10 -> stringResource(Res.string.low_fatigue_symptoms)
        score <= 15 -> stringResource(Res.string.moderate_fatigue_symptoms)
        score <= 20 -> stringResource(Res.string.high_fatigue_symptoms)
        else -> stringResource(Res.string.very_high_fatigue_symptoms)
    }
}

/**
 * Composable function that retrieves string resource for recommendations based on the Double (survey.totalScore) passed to it.
 */
@Composable
fun getFatigueRecommendations(score: Double): String {
    return when {
        score <= 10 -> stringResource(Res.string.low_fatigue_recommendations)
        score <= 15 -> stringResource(Res.string.moderate_fatigue_recommendations)
        score <= 20 -> stringResource(Res.string.high_fatigue_recommendations)
        else -> stringResource(Res.string.very_high_fatigue_recommendations)
    }
}

/**
 * Composable used to give an overview of the various levels of mental fatigue.
 * Color-coded using package wide defined color, as well as a visual indication of where the users score is indicative of.
 */
@Composable
fun LevelIndicator(totalScore: Double) {
    val levels = listOf(
        Triple(stringResource(Res.string.low), 0..10, LevelColor.blue),
        Triple(stringResource(Res.string.moderate), 11..15, LevelColor.green),
        Triple(stringResource(Res.string.high), 16..20, LevelColor.yellow),
        Triple(stringResource(Res.string.very_high), 21..50, LevelColor.red)
    )

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        levels.forEach { (name, scoreRange, color) ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val marker = if (totalScore.roundToInt() in scoreRange) "<< ${totalScore.roundToInt()}" else ""
                Text(text = name, color = color)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = marker, color = color)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}