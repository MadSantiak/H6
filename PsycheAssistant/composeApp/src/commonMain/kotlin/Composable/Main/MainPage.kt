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
import org.psyche.assistant.Composable.LocalAuthToken
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
    // Retrieve surveys from SQL Delight local storage.
    val db = SurveyRepository()
    var surveys by remember { mutableStateOf(db.selectAllSurveys()) }
    val authToken = LocalAuthToken.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Auth Token: ${authToken.value ?: "No Token"}",
            style = MaterialTheme.typography.h6,
            color = Color.Gray,
            modifier = Modifier.padding(16.dp)
        )
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
