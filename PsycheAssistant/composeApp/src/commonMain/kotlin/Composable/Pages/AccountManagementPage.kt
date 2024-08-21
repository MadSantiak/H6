package org.psyche.assistant.Composable.Pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.psyche.assistant.Composable.LocalAuthToken
import org.psyche.assistant.Composable.Sections.GroupManagementSection
import org.psyche.assistant.Composable.Sections.UserManagementSection
import psycheassistant.composeapp.generated.resources.Res
import psycheassistant.composeapp.generated.resources.group_management
import psycheassistant.composeapp.generated.resources.sign_in_for_group
import psycheassistant.composeapp.generated.resources.user_management

/**
 * Account management page
 * Used to display various account-related functions, such as user registration/log in, group registration/joining,
 * group administration, as well as group member specific data.
 * Note these are handled by subsequent sections (UserManagementSection, GroupMangementSection).
 */
@Composable
fun AccountManagementPage() {
    val authToken = LocalAuthToken.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(stringResource(Res.string.user_management), style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(8.dp))
        UserManagementSection()

        Spacer(modifier = Modifier.height(32.dp))
        if (authToken.value != null) {

            Text(stringResource(Res.string.group_management), style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.height(8.dp))
            GroupManagementSection()
        } else {
            Text(stringResource(Res.string.sign_in_for_group))
        }
    }
}

