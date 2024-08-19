package org.psyche.assistant.Composable.Pages

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.psyche.assistant.Composable.Sections.EnergyOverviewSection
import org.psyche.assistant.Composable.Sections.SurveyHistorySection
import org.psyche.assistant.Model.SurveyRepository

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


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            EnergyOverviewSection()

            Spacer(modifier = Modifier.height(32.dp))

            SurveyHistorySection(
                surveys = surveys,
                onDeleteSurvey = { surveyId ->
                    db.deleteSurveyById(surveyId)
                    surveys = db.selectAllSurveys()
                }
            )

        }
    }
}
