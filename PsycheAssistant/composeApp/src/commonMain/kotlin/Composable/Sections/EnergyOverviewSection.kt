package org.psyche.assistant.Composable.Sections

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import network.chaintech.kmp_date_time_picker.utils.now
import org.jetbrains.compose.resources.stringResource
import org.psyche.assistant.Composable.Components.EnergyExpenditureChart
import org.psyche.assistant.Composable.LocalAuthToken
import org.psyche.assistant.Composable.LocalGroup
import org.psyche.assistant.Composable.LocalUser
import org.psyche.assistant.Controller.ActivityController
import org.psyche.assistant.Model.Activity.Activity
import org.psyche.assistant.Model.User.User
import psycheassistant.composeapp.generated.resources.*
import roundToDecimals

/**
 * Energy overview section
 * Used to display user-specific energy expenditure values, if specified.
 * It can be used to display a simplified summation of daily/weekly avg. expenditure, or a more detailed
 * version including graph visualization, determined by whether the function is explicitly called with "simple=true".
 * If it's called with a specificUser designated, this overwrites the logged-in user, used when generating list-views of group members.
 * @param simple
 * @param specificUser
 */
@Composable
fun EnergyOverviewSection(simple: Boolean = false, specificUser: User? = null) {
    // Values to fetch relevant completed activities, and show today's energy expenditure.
    val authToken = LocalAuthToken.current
    val group = LocalGroup.current
    val activityController = ActivityController()
    val unknownError = stringResource(Res.string.unknown_error)

    // User is converted to variable so it can be reassigned, based on whether a specific user is passed to the function.
    var user = LocalUser.current
    var weeklyActivities by remember { mutableStateOf<Map<String, List<Activity>>>(emptyMap()) }
    var energyExpenditure by remember { mutableStateOf(0.0) }
    var energyExpenditureToday by remember { mutableStateOf(0.0) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    if (specificUser != null)
    {
        user = remember {mutableStateOf(specificUser)}
    }
    LaunchedEffect(authToken) {
        if (authToken.value != null) {
            try {
                var activities = activityController.getHandledActivityByPeriod(
                    group.value!!.id,
                    LocalDate.now(),
                    LocalDate.now().minus(DatePeriod(days = 7)
                ))

                activities.forEach { activity ->
                    energyExpenditureToday += if (activity.handledOn == LocalDate.now() && activity.completed && activity.handledById == user.value?.id) activity.energyCost else 0
                    energyExpenditure += if (activity.completed && activity.handledById == user.value?.id) activity.energyCost else 0
                }
                weeklyActivities = activities.filter { it.completed && it.handledById == user.value?.id}.groupBy {it.handledOn.toString()}

            } catch (e: Exception) {
                errorMessage = e.message ?: unknownError
            } finally {
                isLoading = false
            }
        }
    }

    if (simple) {
        Text(
            text = "${energyExpenditureToday} / ${(energyExpenditure / 7).roundToDecimals(1)}"
        )
    }
    else {
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
                    stringResource(Res.string.energy_consumption),
                    style = MaterialTheme.typography.h4
                )
                Divider(
                    modifier = Modifier
                        .height(4.dp)
                )
                Text(
                    stringResource(Res.string.today, energyExpenditureToday)
                )
                Text(
                    stringResource(Res.string.past_week, (energyExpenditure / 7).roundToDecimals(1))
                )
                EnergyExpenditureChart(data = weeklyActivities)
            }

        }

    }
}