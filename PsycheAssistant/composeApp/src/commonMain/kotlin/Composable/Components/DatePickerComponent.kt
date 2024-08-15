package org.psyche.assistant.Composable.Components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerView
import network.chaintech.kmp_date_time_picker.utils.DateTimePickerView
import org.jetbrains.compose.resources.stringResource
import psycheassistant.composeapp.generated.resources.Res
import psycheassistant.composeapp.generated.resources.select_date
import psycheassistant.composeapp.generated.resources.selected_date

@Composable
fun DatePickerComponent(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(initialDate) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(Res.string.selected_date, selectedDate.toString())
        )
        Button(
            onClick = { showDatePicker = true },

        ) {
            Text(text = stringResource(Res.string.select_date))
        }

        if (showDatePicker) {
            WheelDatePickerView(
                showDatePicker = showDatePicker,
                rowCount = 5,
                height = 200.dp,
                title = "Select Date",
                dateTimePickerView = DateTimePickerView.DIALOG_VIEW,
                dragHandle = {
                    TabRowDefaults.Divider(
                        modifier = Modifier.padding(top = 8.dp).width(15.dp).clip(CircleShape),
                        thickness = 4.dp,
                    )
                },
                onDoneClick = {
                    selectedDate = it
                    onDateSelected(it)
                    showDatePicker = false
                },
                onDismiss = {
                    onDismiss()
                    showDatePicker = false
                }
            )
        }
    }
}