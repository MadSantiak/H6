package org.psyche.assistant.Composable.Dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.utils.now
import org.jetbrains.compose.resources.stringResource
import org.psyche.assistant.Composable.Components.DatePickerComponent
import psycheassistant.composeapp.generated.resources.*

/**
 * Activity create dialog
 * A dialog used for creating new activities, passing the functions needed to submit the new activity/dismiss the dialog
 * Using coroutine to asynchronously attempt to create the activity using onSubmit, as well as the variables
 * @param onDismiss
 * @param onSubmit
 */
@Composable
fun ActivityCreateDialog(
    onDismiss: () -> Unit,
    onSubmit: (LocalDate, Float, String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var energyCost by remember { mutableStateOf(0f) }
    var description by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    fun handleSubmit() {
        isSubmitting = true
        if (description.trim().isEmpty()) {
            errorMessage = "Description cannot be empty"
            isSubmitting = false
            return
        }
        coroutineScope.launch {
            try {
                onSubmit(selectedDate, energyCost, description)
                onDismiss()
            } catch (e: Exception) {
                errorMessage = e.message ?: "An error occurred"
            } finally {
                isSubmitting = false
            }
        }
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(stringResource(Res.string.create_activity)) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                // Datepicker component, includes button to activate the component.
                DatePickerComponent(
                    initialDate = selectedDate,
                    onDateSelected = { date -> selectedDate = date },
                    onDismiss = { }
                )

                Divider(
                    modifier = Modifier
                        .padding(vertical = 15.dp),
                    thickness = 4.dp
                )

                // Energy Cost Slider
                Text(stringResource(Res.string.select_energy_cost))
                Slider(
                    value = energyCost,
                    onValueChange = { energyCost = it },
                    valueRange = 0f..10f,
                    steps = 10
                )
                Text(stringResource(Res.string.energy_cost, energyCost.toInt()))

                Divider(
                    modifier = Modifier
                        .padding(vertical = 15.dp),
                    thickness = 4.dp
                )

                Text(stringResource(Res.string.description))
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("...") }
                )

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = { handleSubmit() }, enabled = !isSubmitting) {
                Text(stringResource(Res.string.create_activity))
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text(stringResource(Res.string.cancel))
            }
        }
    )
}