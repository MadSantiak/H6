package org.psyche.assistant.Composable.Sections

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    Box(
        modifier = Modifier
            .border(BorderStroke(1.dp, Color.Gray))
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                stringResource(Res.string.survey),
                style = MaterialTheme.typography.h4
            )

            Divider(modifier = Modifier
                .height(4.dp))

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
                Spacer(modifier = Modifier.width(40.dp))
            }
            surveys.sortedByDescending { it.date }.forEach { survey ->
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

                    IconButton(onClick = { onDeleteSurvey(survey.id) }) {
                        Icon(Icons.Filled.Delete, contentDescription = stringResource(Res.string.delete))
                    }
                }
            }
        }
    }
}
