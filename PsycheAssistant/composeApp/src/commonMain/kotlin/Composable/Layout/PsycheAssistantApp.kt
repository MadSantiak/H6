package org.psyche.assistant.Composable.Layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import org.psyche.assistant.Composable.LocalAuthToken
import org.psyche.assistant.Composable.LocalUser
import org.psyche.assistant.Composable.Main.MainPage
import org.psyche.assistant.Composable.Settings.SettingsPage
import org.psyche.assistant.Composable.Survey.SurveyPage
import org.psyche.assistant.Controller.UserController
import org.psyche.assistant.Helper.CustomTheme
import org.psyche.assistant.Helper.GlobalLocaleState
import org.psyche.assistant.Model.User.User
import org.psyche.assistant.Storage.AuthStorage
import psycheassistant.composeapp.generated.resources.Res
import psycheassistant.composeapp.generated.resources.*


/**
 * Composable that controls the screen, adding a nav-bar at the top and a switch mechanism for navigating between them.
 * I.e. when one item is clicked, the "currentScreen" variable changes, and the appropriate Composable is called.
 */
@Composable
fun PsycheAssistantApp() {
    var currentScreen by remember { mutableStateOf("main") }
    var authToken = remember { mutableStateOf(AuthStorage.getAuthToken()) }
    val userState = remember { mutableStateOf<User?>(null) }

    LaunchedEffect(authToken.value) {
        authToken.value?.let { token ->
            try {
                val userDetails = UserController().getUserDetails(token)
                userState.value = userDetails
            } catch (e: Exception) {


                // handle error.. mkay?
            }
        }
    }
    MaterialTheme(
        /**
         * Wrap the entire contents in the custom color theme.
         */
        colors = CustomTheme.psycheColors()
    ) {
        // Get the GlobalState value and pass it along to the compositions.
        CompositionLocalProvider(
            LocalAuthToken provides authToken,
            LocalUser provides userState
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
                        BottomNavigationItem(
                            icon = { Icon(Icons.Default.Done, contentDescription = null) },
                            label = { Text(stringResource(Res.string.settings)) },
                            selected = currentScreen == "settings",
                            onClick = { currentScreen = "settings" }
                        )
                    }
                },

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
                           "settings" -> SettingsPage()
                        }
                    }
                }
            )
        }
    }
}


