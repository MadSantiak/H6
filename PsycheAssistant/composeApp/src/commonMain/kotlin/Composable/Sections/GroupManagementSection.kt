package org.psyche.assistant.Composable.Sections

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.psyche.assistant.Composable.Dialogs.ConfirmDeleteDialog
import org.psyche.assistant.Composable.Items.UserItemTable
import org.psyche.assistant.Composable.LocalAuthToken
import org.psyche.assistant.Composable.LocalGroup
import org.psyche.assistant.Composable.LocalUser
import org.psyche.assistant.Controller.GroupController
import org.psyche.assistant.Controller.UserController
import org.psyche.assistant.Model.User.User
import psycheassistant.composeapp.generated.resources.*

/**
 * Group management section
 * Used for group-specific administrative tasks and overview. I.e. creating or joining a group,
 * as well as seeing an overview of the groups members, and being able to kick them (if the user is the owner of the group).
 *
 */
@Composable
fun GroupManagementSection() {
    val coroutineScope = rememberCoroutineScope()
    val groupController = GroupController()
    val userController = UserController()

    var authToken = LocalAuthToken.current
    var group = LocalGroup.current
    var user = LocalUser.current
    var code by remember { mutableStateOf("")}
    var members by remember { mutableStateOf<List<User>>(emptyList())}

    var showConfirmationDialog by remember { mutableStateOf(false) }
    var isOwner by remember { mutableStateOf(false)}
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var unknownError = stringResource(Res.string.unknown_error)
    var failedToKick = stringResource(Res.string.failed_to_kick_member)
    var noCodeOrAuth = stringResource(Res.string.no_code_or_auth)
    var membersAmount = stringResource(Res.string.members_amount, members.size)


    LaunchedEffect(authToken, group.value, members) {
        if (authToken.value != null) {
            isLoading = true
            try {
                val groupDetails = groupController.getGroupDetails(group.value!!.id)
                group.value = groupDetails
                var groupOwner = userController.getUserById(groupDetails?.ownerId!!)
                isOwner = groupOwner?.id == user.value?.id
                members = groupDetails.userIds
                    .mapNotNull { userId -> userController.getUserById(userId) }
            } catch (e: Exception) {
                errorMessage = e.message ?: unknownError
            } finally {
                isLoading = false
            }
        }
    }

    fun kickMember(user: User) {
        coroutineScope.launch {
            try {
                isLoading = true
                val result = groupController.kickMember(authToken.value!!, group.value!!.id, user.id)
                if (result != null) {
                    members = members.filter { it.id != user.id }
                } else {
                    errorMessage = failedToKick
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: unknownError
            } finally {
                isLoading = false
            }
        }
    }

    val disbandGroup = {
        coroutineScope.launch {
            try {
                isLoading = true
                val result = groupController.disbandGroup(authToken.value!!, group.value!!.id)
                if (result) {
                    group.value = null
                    members = emptyList()
                } else {
                    errorMessage = "Failed to disband group."
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: unknownError
            } finally {
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
        Text(stringResource(Res.string.group, group.value?.code ?: stringResource(Res.string.no_group)), style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))

        if (authToken.value != null && group.value != null) {

            Row {
                Button(onClick = {
                    coroutineScope.launch {
                        try {
                            isLoading = true
                            val result = groupController.leaveGroup(authToken.value!!, group.value!!.id)
                            if (result) {
                                group.value = null
                                members = emptyList()
                            } else {
                                errorMessage = "Testing.."
                            }
                        } catch (e: Exception) {
                            errorMessage = e.message ?: unknownError
                        } finally {
                            isLoading = false
                        }
                    }
                }) {
                    Text(text = stringResource(Res.string.leave_group))
                }
                Spacer(modifier = Modifier.padding(15.dp))
                if (isOwner) {
                    Button(
                        onClick = {
                            showConfirmationDialog = true
                        }, colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Red,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(text = stringResource(Res.string.disband_group))
                    }
                }
            }
        }

        if (authToken.value != null && group.value == null) {
            Button(onClick = {
                coroutineScope.launch {
                    try {
                        isLoading = true
                        val groupDetails = groupController.createGroup(authToken.value!!)
                        group.value = groupDetails
                    } catch (e: Exception) {
                        errorMessage = e.message ?: unknownError
                    } finally {
                        isLoading = false
                    }
                }
            }) {
                Text(text = stringResource(Res.string.create_group))
            }

            Text(stringResource(Res.string.join_group), style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                label = { Text(stringResource(Res.string.group_code)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                if (code.isNotBlank() && authToken.value != null) {
                    isLoading = true
                    coroutineScope.launch {
                        try {
                            val joinedGroup = groupController.joinGroup(authToken.value!!, code)
                            group.value = joinedGroup?.let { groupController.getGroupDetails(it.id) }
                        } catch (e: Exception) {
                            errorMessage = e.message ?: unknownError
                        } finally {
                            isLoading = false
                        }
                    }
                } else {
                    errorMessage = noCodeOrAuth
                }
            }) {
                Text(stringResource(Res.string.join_group))
            }


        }
        Text(text = membersAmount)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            if (members.isNullOrEmpty()) {
                item {
                    Text(text = stringResource(Res.string.no_members))
                }
            } else {
                item {
                    UserItemTable(
                        users = members,
                        onKickClick = { user -> kickMember(user) },
                        showKickButton = isOwner,
                        currentUserId = user.value?.id,
                        ownerId = group.value?.ownerId
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (isLoading) {
            CircularProgressIndicator()
        }

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red)
        }

        if (showConfirmationDialog) {
            ConfirmDeleteDialog(
                title = stringResource(Res.string.confirm_disband),
                message = stringResource(Res.string.disband_confirm_intent),
                confirmButtonText = stringResource(Res.string.disband),
                dismissButtonText = stringResource(Res.string.cancel),
                onConfirm = { disbandGroup() },
                onDismiss = { showConfirmationDialog = false }
            )
        }

    }
}
