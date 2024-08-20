package org.psyche.assistant.Composable.Items

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.psyche.assistant.Model.Activity.Activity
import psycheassistant.composeapp.generated.resources.Res
import psycheassistant.composeapp.generated.resources.activity
import psycheassistant.composeapp.generated.resources.energy

@Composable
fun ActivityItemTable(
    activities: List<Activity>,
    onCompleteClick: (Activity) -> Unit,
    onDeleteClick: (Activity) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(Res.string.activity),
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(Res.string.energy),
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.weight(1f)
            )
            // Spaces for buttons
            Spacer(modifier = Modifier.width(40.dp))
            Spacer(modifier = Modifier.width(40.dp))
        }
        Divider()

        activities.forEach { activity ->
            ActivityItem(
                activity = activity,
                onCompleteClick = onCompleteClick,
                onDeleteClick = onDeleteClick,
            )
            Divider()
        }
    }
}