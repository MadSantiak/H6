package org.psyche.assistant.Composable.Main

import SurveyHistory
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.psyche.assistant.Composable.LocalAuthToken
import org.psyche.assistant.Composable.LocalGroup
import org.psyche.assistant.Composable.LocalUser
import org.psyche.assistant.Model.SurveyRepository
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
    val group = LocalGroup.current
    val user = LocalUser.current

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
        Text(
            text = "Group: ${group.value?.code ?: "No Group"}",
            style = MaterialTheme.typography.h6,
            color = Color.Gray,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = "Logged in as ${user.value?.email}",
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
