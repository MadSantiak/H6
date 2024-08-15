package org.psyche.assistant.Composable.Pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.utils.now
import org.jetbrains.compose.resources.stringResource
import org.psyche.assistant.Composable.Dialogs.ActivityCreateDialog
import org.psyche.assistant.Composable.Items.ActivityItem
import org.psyche.assistant.Composable.LocalAuthToken
import org.psyche.assistant.Composable.LocalGroup
import org.psyche.assistant.Controller.ActivityController
import org.psyche.assistant.Model.Activity.Activity
import psycheassistant.composeapp.generated.resources.Res
import psycheassistant.composeapp.generated.resources.unknown_error

@Composable
fun ActivityPage() {
    val authToken = LocalAuthToken.current
    val group = LocalGroup.current
    val activityController = ActivityController()
    val coroutineScope = rememberCoroutineScope()

    var activities by remember { mutableStateOf<List<Activity>>(emptyList())}

    var isDialogOpen by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf("") }
    var unknownError = stringResource(Res.string.unknown_error)


    LaunchedEffect(authToken.value, activities) {
        if (authToken.value != null) {
            isLoading = true
            try {
                activities = activityController.getTodayActivityForGroup(group.value!!.id, LocalDate.now())
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
        Button(onClick = { isDialogOpen = true }) {
            Text("Create New Activity")
        }

        LazyColumn {
            items(activities) { activity ->
                ActivityItem(activity)
            }
        }
    }
    if (isDialogOpen) {
        ActivityCreateDialog(
            onDismiss = { isDialogOpen = false },
            onSubmit = { date, cost, desc ->
                coroutineScope.launch {
                    try {
                        activityController.createActivity(authToken.value!!, date.toString(), desc, cost.toInt())
                        activities = activityController.getTodayActivityForGroup(group.value!!.id, LocalDate.now())
                    } catch (e: Exception) {
                        errorMessage = e.message ?: unknownError
                    }
                }
            }
        )
    }
}