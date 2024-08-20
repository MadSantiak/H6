package org.psyche.assistant.Composable.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import network.chaintech.kmp_date_time_picker.utils.now
import org.jetbrains.compose.resources.stringResource
import org.psyche.assistant.Model.Activity.Activity
import psycheassistant.composeapp.generated.resources.Res
import psycheassistant.composeapp.generated.resources.weekly_overview

@Composable
fun EnergyExpenditureChart(data: Map<String, List<Activity>>) {
    if (data.isEmpty()) {
        return
    }

    val chartHeight = 150.dp
    val textStyle = TextStyle(color = Color.Gray, fontSize = 10.sp)
    val padding = 15.dp

    // Calculate the date range for the last 7 days
    val today = LocalDate.now()
    val dateRange = (0 until 7).map { daysBack ->
        today.minus(DatePeriod(days = daysBack))
    }.reversed().map { it.toString() }

    // Generate the daily expenditure map
    val dailyExpenditure = dateRange.associateWith { date ->
        data[date]?.sumOf { it.energyCost.toDouble() } ?: 0.0
    }

    Column(
        modifier = Modifier
            .border(BorderStroke(1.dp, Color.Gray))
            .fillMaxWidth()
            .padding(bottom = 16.dp)

    ) {
        // Header text
        Text(
            text = stringResource(Res.string.weekly_overview),
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
        )

        Divider()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .height(150.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width / 7f // Fixed number of bars (7)
                val maxEnergy = dailyExpenditure.values.maxOrNull() ?: 1.0 // Avoid division by zero

                dailyExpenditure.toList().forEachIndexed { index, (_, energy) ->
                    val barHeight = (energy / maxEnergy) * size.height

                    drawRect(
                        color = Color.Green,
                        topLeft = Offset(x = index * width, y = size.height - barHeight.toFloat()),
                        size = Size(width - 4.dp.toPx(), barHeight.toFloat())
                    )
                }
            }
        }

        Divider()

        // Dates and Values
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = padding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            dailyExpenditure.keys.forEach { date ->
                val energy = dailyExpenditure[date] ?: 0.0

                Text(
                    text = date.split("-").let {
                        "${it[2]}/${it[1]}\n${energy}"
                    },
                    style = textStyle,
                    modifier = Modifier
                        .weight(1f),
                    textAlign = TextAlign.Center

                )
            }
        }
    }
}
