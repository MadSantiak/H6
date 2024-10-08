package org.psyche.assistant.Composable.Sections

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.psyche.assistant.Composable.Dialogs.ConfirmDeleteDialog
import org.psyche.assistant.Composable.LocalAuthToken
import org.psyche.assistant.Composable.LocalGroup
import org.psyche.assistant.Composable.LocalUser
import org.psyche.assistant.Controller.UserController
import psycheassistant.composeapp.generated.resources.*

/**
 * User management section
 * Used to manage user-specific settings. I.e. registering and/or logging in/out of a specific user.
 */
@Composable
fun UserManagementSection() {
    val coroutineScope = rememberCoroutineScope()
    val userController = UserController()

    var authToken = LocalAuthToken.current
    var user = LocalUser.current
    var group = LocalGroup.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var unknownError = stringResource(Res.string.unknown_error)
    var noEmailOrPass = stringResource(Res.string.no_email_or_pass)
    var showConfirmationDialog by remember { mutableStateOf(false) }

    val deleteUser = {
        isLoading = true
        coroutineScope.launch {
            try {
                userController.deleteUser(authToken.value!!)
                authToken.value = null
                user.value = null
                group.value = null
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
        if (authToken.value != null) {
            Text(
                text = stringResource(Res.string.logged_in_as, user.value?.email ?: ""),
                style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Button(onClick = {
                    isLoading = true
                    coroutineScope.launch {
                        try {
                            userController.signOutUser()
                            authToken.value = null
                            user.value = null
                            group.value = null
                        } catch (e: Exception) {
                            errorMessage = e.message ?: unknownError
                        } finally {
                            isLoading = false
                        }
                    }
                }) {
                    Text(stringResource(Res.string.logout))
                }
                Spacer(modifier = Modifier.padding(15.dp))
                Button(
                    onClick = {
                        showConfirmationDialog = true
                    }, colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Red,
                        contentColor = Color.Black
                    )
                ) {
                    Text(stringResource(Res.string.delete_user))
                }
            }
        } else {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(Res.string.email)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(Res.string.password)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    isLoading = true
                    coroutineScope.launch {
                        try {
                            authToken.value = userController.authenticateUser(email, password)
                            user.value = userController.getUserProfile(authToken.value!!)
                        } catch (e: Exception) {
                            errorMessage = e.message ?: unknownError
                        } finally {
                            isLoading = false
                        }
                    }
                } else {
                    errorMessage = noEmailOrPass
                }
            }) {
                Text(stringResource(Res.string.login))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    isLoading = true
                    coroutineScope.launch {
                        try {
                            userController.registerNewUser(email, password)
                            authToken.value = userController.authenticateUser(email, password)
                        } catch (e: Exception) {
                            errorMessage = e.message ?: unknownError
                        } finally {
                            isLoading = false
                        }
                    }
                } else {
                    errorMessage = noEmailOrPass
                }
            }) {
                Text(stringResource(Res.string.register))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (isLoading) {
            CircularProgressIndicator()
        }

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red)
        }
    }

    if (showConfirmationDialog) {
        ConfirmDeleteDialog(
            title = stringResource(Res.string.confirm_delete),
            message = stringResource(Res.string.delete_confirm_intent),
            confirmButtonText = stringResource(Res.string.delete),
            dismissButtonText = stringResource(Res.string.cancel),
            onConfirm = { deleteUser() },
            onDismiss = { showConfirmationDialog = false }
        )
    }
}