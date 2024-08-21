package org.psyche.assistant.Composable.Items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.psyche.assistant.Model.Activity.Activity
import psycheassistant.composeapp.generated.resources.Res
import psycheassistant.composeapp.generated.resources.complete
import psycheassistant.composeapp.generated.resources.delete

/**
 * Activity item
 * Composable used to display a single Activity record, passed to it as a parameter.
 * Note this exists as part of a table structure, and is therefore arranged in rows.
 * @param activity
 * @param onCompleteClick
 * @param onDeleteClick
 */
@Composable
fun ActivityItem(
    activity: Activity,
    onCompleteClick: (Activity) -> Unit,
    onDeleteClick: (Activity) -> Unit,
    ) {

    val isCompleted = activity.completed

    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            // Append a second modifier in order to strike through the entire row, if the activity is completed.
            .then(
                if (activity.completed) Modifier.drawBehind {
                    val strokeWidth = 2.dp.toPx()
                    val yOffset = size.height / 2
                    drawLine(
                        color = Color.Gray,
                        start = androidx.compose.ui.geometry.Offset(0f, yOffset),
                        end = androidx.compose.ui.geometry.Offset(size.width, yOffset),
                        strokeWidth = strokeWidth
                    )
                } else Modifier
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,

        )
    {
        Text(
            text = activity.description,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                    .weight(2f)
        )
        Text(
            text = activity.energyCost.toString(),
            style = MaterialTheme.typography.body2,
            modifier = Modifier
                .weight(1f)
        )
        IconButton(
            onClick = {
                if (!isCompleted) {
                    onCompleteClick(activity)
                }
            },
            enabled = !isCompleted
        ) {
            Icon(Icons.Filled.Check, contentDescription = stringResource(Res.string.complete))
        }
        IconButton(onClick = { onDeleteClick(activity) }) {
            Icon(Icons.Filled.Delete, contentDescription = stringResource(Res.string.delete))
        }
    }
}