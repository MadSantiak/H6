package org.psyche.assistant.Composable.Sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import org.psyche.assistant.Helper.DateFormatHelper
import org.psyche.assistant.Helper.LevelColor
import org.psyche.assistant.Survey
import psycheassistant.composeapp.generated.resources.*

/**
 * Composable for listing surveys.
 * Note: This uses the "Survey" database table (named Survey), not the Survey model (SurveyModel).
 * This is a limitation of the way SQL Delight auto-generates classes.
 */
@Composable
fun SurveyHistorySection(surveys: List<Survey>, onDeleteSurvey: (Long) -> Unit) {
    if (surveys.isEmpty()) {
        return
    }

    Text(stringResource(Res.string.previous_results), style = MaterialTheme.typography.h6)
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(Res.string.date),
            fontSize = 22.sp,
            modifier = Modifier.weight(3f)
        )
        Text(
            text = stringResource(Res.string.score),
            fontSize = 22.sp,
            modifier = Modifier.weight(1f)
        )
    }
    surveys.sortedByDescending{it.date}.forEach { survey ->
        val dateTime = survey.date?.let { DateFormatHelper.formatDateTime(it) }

        // Note that we here assert that totalScore is not null, by using Kotlins not-null operator (!!)
        val scoreColor = LevelColor.getColor(survey.totalScore!!)

        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = dateTime.toString(),
                fontSize = 16.sp,
                modifier = Modifier.weight(3f)
            )
            Text(
                text = survey.totalScore.toString(),
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                color = scoreColor
            )
            Button(onClick = {
                onDeleteSurvey(survey.id)

            }) {
                Text(stringResource(Res.string.delete))
            }
        }
    }

}
