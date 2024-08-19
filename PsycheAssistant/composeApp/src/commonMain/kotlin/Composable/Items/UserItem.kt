package org.psyche.assistant.Composable.Items

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.psyche.assistant.Composable.Sections.EnergyOverviewSection
import org.psyche.assistant.Helper.LevelColor
import org.psyche.assistant.Model.User.User
import psycheassistant.composeapp.generated.resources.Res
import psycheassistant.composeapp.generated.resources.kick_member
import psycheassistant.composeapp.generated.resources.you

@Composable
fun UserItem(
    user: User,
    onKickClick: (User) -> Unit,
    showKickButton: Boolean,
    isCurrentUser: Boolean,
    modifier: Modifier = Modifier
) {


    val textColor = LevelColor.getColor(user.energyExpenditure.toDouble())
    val userIdentifier = if (isCurrentUser) stringResource(Res.string.you) else user.email

    Row(
        modifier = modifier
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = userIdentifier,
            style =  MaterialTheme.typography.body1.copy(color = textColor),
            modifier = Modifier
                .weight(2f)
        )
        //Text(
        //    text = user.energyExpenditure.toString(),
        //    style =  MaterialTheme.typography.body1.copy(color = textColor),
        //    modifier = Modifier.weight(1f)
        //)
        EnergyOverviewSection(simple=true, specificUser = user)
        if (showKickButton) {
            IconButton(onClick = { onKickClick(user) }) {
                Icon(Icons.Filled.Delete, contentDescription = stringResource(Res.string.kick_member))
            }
        } else {
            Spacer(modifier = Modifier.width(40.dp))
        }
    }
}
