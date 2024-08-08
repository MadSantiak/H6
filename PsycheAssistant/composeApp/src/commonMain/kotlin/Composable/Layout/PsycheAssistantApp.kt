package org.psyche.assistant.Composable.Layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import org.psyche.assistant.Composable.Main.MainPage
import org.psyche.assistant.Composable.Survey.SurveyPage
import org.psyche.assistant.Helper.CustomTheme
import org.psyche.assistant.Helper.GlobalLocaleState
import psycheassistant.composeapp.generated.resources.Res
import psycheassistant.composeapp.generated.resources.*


/**
 * Composable that controls the screen, adding a nav-bar at the top and a switch mechanism for navigating between them.
 * I.e. when one item is clicked, the "currentScreen" variable changes, and the appropriate Composable is called.
 */
@Composable
fun PsycheAssistantApp() {
    var currentScreen by remember { mutableStateOf("main") }
    var expanded by remember { mutableStateOf(false) }

    //val english = stringResource(Res.string.english)
    //val danish = stringResource(Res.string.danish)
    MaterialTheme(
        /**
         * Wrap the entire contents in the custom color theme.
         */
        colors = CustomTheme.psycheColors()
    ) {
        Scaffold(
            /**
             * Add a top bar for navigation, passing new values to "currentScreen" when selected, controlling the inner contents.
             */
            topBar = {
                TopAppBar{
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text(stringResource(Res.string.home)) },
                        selected = currentScreen == "main",
                        onClick = { currentScreen = "main" }
                    )
                    BottomNavigationItem(
                        icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
                        label = { Text(stringResource(Res.string.survey)) },
                        selected = currentScreen == "survey",
                        onClick = { currentScreen = "survey" }
                    )
                }
            },
            /**
             * Add a bottom bar which allows for selecting question-language (doesn't affect UI broadly speaking,
             * this is reserved for later development as it would have to be platform specific)
             */
            /**
            bottomBar = {
                BottomAppBar(
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(stringResource(Res.string.language))
                            Box {
                                Text(
                                    text = if (GlobalLocaleState.locale == "en") english else danish,
                                    modifier = Modifier.clickable { expanded = true }
                                )
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    DropdownMenuItem(onClick = {
                                        GlobalLocaleState.locale = "en"
                                        expanded = false
                                    }) {
                                        Text(english)
                                    }
                                    DropdownMenuItem(onClick = {
                                        GlobalLocaleState.locale = "da"
                                        expanded = false
                                    }) {
                                        Text(danish)
                                    }
                                }
                            }
                        }
                    }
                }
            },
            */
            /**
             * Add content, changing depending on the currentScreen value.
             */
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    when (currentScreen) {
                       "main" -> MainPage()
                       "survey" -> SurveyPage(onBack = { currentScreen = "main" })
                    }
                }
            }
        )
    }
}


