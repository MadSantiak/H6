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

/**
 * Energy expenditure chart
 * Component used to visualize a list of activities, sent in data as String/List key-value pairs.
 * with keys being unique dates, and the lists being activities occuring on that date.
 *
 * @param data
 */
@Composable
fun EnergyExpenditureChart(data: Map<String, List<Activity>>) {
    if (data.isEmpty()) {
        return
    }

    val textStyle = TextStyle(color = Color.Gray, fontSize = 10.sp)
    val padding = 15.dp

    // Generate a list of dates, starting from today, going X days back, essentially using a for-loop like structure to do so.
    val today = LocalDate.now()
    val dateRange = (0 until 7).map { daysBack ->
        today.minus(DatePeriod(days = daysBack))
    }.reversed().map { it.toString() }

    // Generate the daily expenditure map by associating each date generated earlier, summing the energyCost of the list of activities in the passed data, for that date.
    val dailyExpenditure = dateRange.associateWith { date ->
        data[date]?.sumOf { it.energyCost.toDouble() } ?: 0.0
    }

    Column(
        modifier = Modifier
            .border(BorderStroke(1.dp, Color.Gray))
            .fillMaxWidth()
            .padding(bottom = 16.dp)

    ) {
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
                val maxEnergy = dailyExpenditure.values.maxOrNull() ?: 1.0

                // For each date-energyExpenditure pair (toList), we draw a rectangle, using the relative energy per day divided by the absolute max of all dates
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

        // Adding a row beneath the Canvas, were we explicitly write out the date and energy expenditure for each bar generated above.
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
