package org.psyche.assistant.Composable.Settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.psyche.assistant.Controller.GroupController
import org.psyche.assistant.Composable.LocalAuthToken
import org.psyche.assistant.Composable.LocalGroup
import org.psyche.assistant.Composable.LocalUser
import org.psyche.assistant.Composable.User.UserItem
import org.psyche.assistant.Controller.UserController
import org.psyche.assistant.Model.User.User
import psycheassistant.composeapp.generated.resources.*


@Composable
fun AccountManagementPage() {
    val authToken = LocalAuthToken.current
    val user = LocalUser.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(stringResource(Res.string.user_management), style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(8.dp))
        UserManagementPage()

        Spacer(modifier = Modifier.height(32.dp))
        if (authToken.value != null) {

            Text(stringResource(Res.string.group_management), style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.height(8.dp))
            GroupManagementPage()
        } else {
            Text(stringResource(Res.string.sign_in_for_group))
        }
    }
}

