package org.psyche.assistant.Composable.Settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.psyche.assistant.Composable.LocalAuthToken
import org.psyche.assistant.Composable.LocalGroup
import org.psyche.assistant.Composable.LocalUser
import org.psyche.assistant.Controller.GroupController
import org.psyche.assistant.Controller.UserController
import org.psyche.assistant.Model.Group.Group
import org.psyche.assistant.Model.SurveyRepository
import org.psyche.assistant.Storage.AuthStorage

/**
 * TO-DO: Populate with relevant settings for the app. E.g. language, delete previous scores, etc.
 */
@Composable
fun SettingsPage()
{
    val coroutineScope = rememberCoroutineScope()
    val userController = UserController()
    val groupController = GroupController()

    var authToken = LocalAuthToken.current
    var user = LocalUser.current
    var group = LocalGroup.current
    // var group = user.value?.group?.let { groupController.getGroupDetails(it) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    //var group by remember { mutableStateOf<Group?>(null)}


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display Auth Token for Development
        Text(
            text = "Auth Token: ${authToken.value ?: "No Token"}",
            style = MaterialTheme.typography.h6,
            color = Color.Gray,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = "Group: ${group.value?.code ?: "No Group"}",
            style = MaterialTheme.typography.h6,
            color = Color.Gray,
            modifier = Modifier.padding(16.dp)
        )
        if (authToken.value != null) {

            Text(
                text = "Logged in as ${user.value?.email}",
                style = MaterialTheme.typography.h6,
                color = Color.Gray,
                modifier = Modifier.padding(16.dp)
            )
            Button(onClick = {
                coroutineScope.launch {
                    val groupDetails = groupController.createGroup(authToken.value!!)
                    group.value = groupDetails
                }
            }) {
                Text(text = "Create Group")
            }
            Button(onClick = {
                isLoading = true
                coroutineScope.launch {
                    try {
                        userController.signOutUser()
                        authToken.value = null
                        user.value = null
                        group.value = null
                    } catch (e: Exception) {
                        errorMessage = e.message ?: "Unknown error"
                    } finally {
                        isLoading = false
                    }
                }

            }) {
                Text("Logout")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text("Register or Login", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if (email.isNotBlank() && password.isNotBlank()) {
                isLoading = true
                coroutineScope.launch {
                    try {
                        userController.registerNewUser(email, password)
                        authToken.value = userController.authenticateUser(email, password)
                    } catch (e: Exception) {
                        errorMessage = e.message ?: "Unknown error"
                    } finally {
                        isLoading = false
                    }
                }
            } else {
                errorMessage = "Email and password cannot be empty"
            }
        }) {
            Text("Register & Login")
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (isLoading) {
            CircularProgressIndicator()
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.body2
            )
        }
    }
}