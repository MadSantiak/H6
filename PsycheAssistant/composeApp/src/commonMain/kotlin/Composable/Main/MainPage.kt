package org.psyche.assistant.Composable.Main

import SurveyHistory
import androidx.compose.foundation.background
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
import psycheassistant.composeapp.generated.resources.Res
import psycheassistant.composeapp.generated.resources.title


/**
 * Main composable. Page meant to hold generalized information on how to use and interact with the app.
 * Using main-page for this as the target usergroup won't necessarily be all too tech-savvy, in addition to
 * potentially exceedingly mentally fatigued.
 */
@Composable
fun MainPage() {
    val db = SurveyRepository()
    var surveys by remember { mutableStateOf(db.selectAllSurveys()) }
    val userController = UserController()

    var userName by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf(-1) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Create New User", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("User Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if (userName.isNotBlank()) {
                isLoading = true
                coroutineScope.launch {
                    try {
                        val newUser = User(id = 0, name = userName, energyExpenditure = 0)
                        userId = userController.addUser(newUser)
                        userName = "" // Clear the input field
                    } catch (e: Exception) {
                        errorMessage = e.message ?: "Unknown error"
                    } finally {
                        isLoading = false
                    }
                }
            } else {
                errorMessage = "Name cannot be empty"
            }
        }) {
            Text("Add User")
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

        if (userId != -1) {
            Text(
                text = "New User ID: $userId",
                style = MaterialTheme.typography.body1
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