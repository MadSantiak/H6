package org.psyche.assistant.Composable.Items

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.psyche.assistant.Model.Activity.Activity

@Composable
fun ActivityItem(activity: Activity) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray)
            .padding(8.dp)
    ) {
        Text(
            text = "Description: ${activity.description}",
            style = MaterialTheme.typography.body1
        )
        Text(
            text = "Energy Cost: ${activity.energyCost}",
            style = MaterialTheme.typography.body2
        )
    }
}