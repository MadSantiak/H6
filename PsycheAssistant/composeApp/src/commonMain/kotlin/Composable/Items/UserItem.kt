package org.psyche.assistant.Composable.User

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.psyche.assistant.Model.User.User

@Composable
fun UserItem(
    user: User,
    onKickClick: (User) -> Unit,
    showKickButton: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Member: ${user.email ?: "Unknown"}")
            Text(text = "Energy Expenditure: ${user.energyExpenditure}")
        }
        Spacer(modifier = Modifier.width(8.dp))
        if (showKickButton) {
            IconButton(onClick = { onKickClick(user) }) {
                Icon(Icons.Filled.Delete, contentDescription = "Kick Member")
            }
        }
    }
}
