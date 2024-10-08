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
import psycheassistant.composeapp.generated.resources.Res
import psycheassistant.composeapp.generated.resources.day_and_week
import psycheassistant.composeapp.generated.resources.energy_expenditure
import psycheassistant.composeapp.generated.resources.member

/**
 * User item table
 * Composable used to display a list of users in a table structure, calling UserItem on each individual user in order to popualte the rows.
 * Note here there are additional parameters to control visibility of Kick-functionality on each user, avoiding showing the option to others
 * than the group owner (and on the group owner themselves, if so).
 * @param users
 * @param onKickClick
 * @param showKickButton
 * @param currentUserId
 * @param ownerId
 * @param modifier
 */
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
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.weight(2f)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(Res.string.energy_expenditure),
                    style = MaterialTheme.typography.subtitle2,

                )
                Text(
                    text = stringResource(Res.string.day_and_week),
                    style = MaterialTheme.typography.subtitle2,
                )
            }
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