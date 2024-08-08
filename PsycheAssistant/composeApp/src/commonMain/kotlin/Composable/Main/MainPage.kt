package org.psyche.assistant.Composable.Main

import SurveyHistory
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.ktor.client.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.psyche.assistant.Controller.ServiceBuilder
import org.psyche.assistant.Controller.UserController
import org.psyche.assistant.Helper.CustomTheme
import org.psyche.assistant.Model.SurveyRepository
import org.psyche.assistant.Model.User.User
import org.psyche.assistant.Storage.AuthStorage
import psycheassistant.composeapp.generated.resources.Res
import psycheassistant.composeapp.generated.resources.title

/**
 * Main composable. Page meant to hold generalized information on how to use and interact with the app.
 * Using main-page for this as the target usergroup won't necessarily be all too tech-savvy, in addition to
 * potentially exceedingly mentally fatigued.
 */
@Composable
fun MainPage() {
    // Retrieve token on composable load
    val authToken by remember { mutableStateOf(AuthStorage.getAuthToken()) }
    val coroutineScope = rememberCoroutineScope()
    val db = SurveyRepository()
    val userController = UserController()

    // Retrieve surveys from SQL Delight local storage.
    var surveys by remember { mutableStateOf(db.selectAllSurveys()) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }



    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display Auth Token for Development
        Text(
            text = "Auth Token: ${authToken ?: "No Token"}",
            style = MaterialTheme.typography.h6,
            color = Color.Gray,
            modifier = Modifier.padding(16.dp)
        )

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
                        userController.registerUser(email, password)
                        // After registration, automatically log in
                        val token = userController.loginUser(email, password)
                        // Token is already saved in AuthStorage
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(stringResource(Res.string.title), style = MaterialTheme.typography.h4)
            Spacer(modifier = Modifier.height(16.dp))
            SurveyHistory(
                surveys = surveys,
                onDeleteSurvey = { surveyId ->
                    db.deleteSurveyById(surveyId)
                    surveys = db.selectAllSurveys()
                }
            )
        }
    }
}
