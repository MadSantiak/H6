package org.psyche.assistant.Composable.Items

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.psyche.assistant.Model.User.User
import psycheassistant.composeapp.generated.resources.*

@Composable
fun UserItemTable(
    users: List<User>,
    onKickClick: (User) -> Unit,
    showKickButton: Boolean,
    currentUserId: Int?,
    ownerId: Int?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(Res.string.member),
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(Res.string.energy_expenditure),
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.weight(1f)
            )
            if (showKickButton) {
                Spacer(modifier = Modifier.width(40.dp)) // Space for the Kick button
            }
        }
        Divider()

        users.forEach { user ->
            UserItem(
                user = user,
                onKickClick = onKickClick,
                showKickButton = showKickButton && user.id != ownerId,
                isCurrentUser = user.id == currentUserId,
                modifier = Modifier.fillMaxWidth()
            )
            Divider()
        }
    }
}