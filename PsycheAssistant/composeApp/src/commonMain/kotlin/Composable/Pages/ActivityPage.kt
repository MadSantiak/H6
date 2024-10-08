package org.psyche.assistant.Composable.Pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import network.chaintech.kmp_date_time_picker.utils.now
import org.jetbrains.compose.resources.stringResource
import org.psyche.assistant.Composable.Dialogs.ActivityCreateDialog
import org.psyche.assistant.Composable.Items.ActivityItemTable
import org.psyche.assistant.Composable.LocalAuthToken
import org.psyche.assistant.Composable.LocalGroup
import org.psyche.assistant.Controller.ActivityController
import org.psyche.assistant.Model.Activity.Activity
import psycheassistant.composeapp.generated.resources.*

/**
 * Activity page
 * Used to administrate activities, ensuring the list of activities are updated when needed,
 * that they can be created/completed/deleted, and that the user can paginate through dates to see activities from other days.
 * Note that if they choose to complete activities from a different date, the activation counts for the day they were completed,
 * not the deadline they had.
 */
@Composable
fun ActivityPage() {
    val authToken = LocalAuthToken.current
    val group = LocalGroup.current
    val activityController = ActivityController()
    val coroutineScope = rememberCoroutineScope()

    var currentDate by remember { mutableStateOf(LocalDate.now())}
    var activities by remember { mutableStateOf<List<Activity>>(emptyList())}

    var isWarningDialogOpen by remember { mutableStateOf(false) }
    var isDialogOpen by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf("") }
    var unknownError = stringResource(Res.string.unknown_error)
    var failedDeleteError = stringResource(Res.string.failed_delete_activity)

    LaunchedEffect(authToken.value, activities, currentDate) {
        if (authToken.value != null) {
            isLoading = true
            try {
                activities = activityController.getActivityForToday(group.value!!.id, currentDate)
            } catch (e: Exception) {
                errorMessage = e.message ?: unknownError
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Complete activity
     * Launches asynchronous operation to mark the activity as completed by the current user (i.e. matching the authToken)
     * @param activity
     */
    fun completeActivity(activity: Activity) {
        coroutineScope.launch {
            try {
                isLoading = true
                val result = activityController.completeActivity(authToken.value!!, activity.id)
                if (result != null) {
                    activities = activities.filter { it.id != activity.id }
                } else {
                    errorMessage = "Herp"
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: unknownError
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Delete activity
     * Launches asynchronous operation to delete the activity.
     * @param activity
     */
    fun deleteActivity(activity: Activity) {
        coroutineScope.launch {
            try {
                isLoading = true
                val activityId = activity.id
                val result = activityController.deleteActivity(authToken.value!!, activity.id)
                if (result) {
                    activities = activities.filter { it.id != activityId }
                } else {
                    errorMessage = failedDeleteError
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: unknownError
            } finally {
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {

            Button(onClick = { currentDate = currentDate.minus(DatePeriod(days = 1)) }) {
                Text("<")
            }

            Text(currentDate.toString(), modifier = Modifier.align(Alignment.CenterVertically))
            Button(onClick = { currentDate = currentDate.plus(DatePeriod(days = 1)) }) {
                Text(">")
            }
        }

        Button(onClick = {
            if (group.value == null) {
                isWarningDialogOpen = true
            } else {
                isDialogOpen = true
            }
        }) {
            Text(stringResource(Res.string.create_activity))
        }

        // Add a scrollable column for the activities
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            if (activities.isNotEmpty()) {
                item {
                    ActivityItemTable(
                        activities = activities,
                        onCompleteClick = { activity -> completeActivity(activity) },
                        onDeleteClick = { activity -> deleteActivity(activity) }
                    )
                }
            }
        }
    }

    // Call the Create Activity dialog when the boolean changes, and change it back once the create dialog is dismissed.
    if (isDialogOpen) {
        ActivityCreateDialog(
            onDismiss = { isDialogOpen = false },
            onSubmit = { date, cost, desc ->
                coroutineScope.launch {
                    try {
                        activityController.createActivity(authToken.value!!, date.toString(), desc, cost.toInt())
                        activities = activityController.getActivityForToday(group.value!!.id, LocalDate.now())
                    } catch (e: Exception) {
                        errorMessage = e.message ?: unknownError
                    }
                }
            }
        )
    }
    if (isWarningDialogOpen) {
        AlertDialog(
            onDismissRequest = { isWarningDialogOpen = false },
            title = { Text(stringResource(Res.string.no_group)) },
            text = { Text(stringResource(Res.string.missing_group_warning)) },
            confirmButton = {
                Button(onClick = { isWarningDialogOpen = false }) {
                    Text(stringResource(Res.string.okay))
                }
            }
        )
    }
}