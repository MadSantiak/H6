package org.psyche.assistant.Composable.Main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.twotone.CheckCircle
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.psyche.assistant.Composable.Common.LoadingScreen
import org.psyche.assistant.Composable.LocalAuthToken
import org.psyche.assistant.Composable.LocalGroup
import org.psyche.assistant.Composable.LocalUser
import org.psyche.assistant.Composable.Pages.AccountManagementPage
import org.psyche.assistant.Composable.Pages.ActivityPage
import org.psyche.assistant.Composable.Pages.MainPage
import org.psyche.assistant.Composable.Pages.SurveyPage
import org.psyche.assistant.Controller.GroupController
import org.psyche.assistant.Controller.UserController
import org.psyche.assistant.Helper.CustomTheme
import org.psyche.assistant.Model.Group.Group
import org.psyche.assistant.Model.User.User
import org.psyche.assistant.Storage.AuthStorage
import psycheassistant.composeapp.generated.resources.*

@Composable
fun PsycheAssistantApp() {
    var currentScreen by remember { mutableStateOf("main") }
    var authToken = remember { mutableStateOf(AuthStorage.getAuthToken()) }
    val userState = remember { mutableStateOf<User?>(null) }
    val groupState = remember { mutableStateOf<Group?>(null) }


    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(authToken.value) {
        if (authToken.value != null) {
            try {
                val token = authToken.value!!
                val userDetails = UserController().getUserProfile(token)
                userState.value = userDetails
                val groupDetails = userDetails?.groupId?.let { GroupController().getGroupDetails(it) }
                groupState.value = groupDetails
                isLoading = false
            } catch (e: Exception) {
                isLoading = false
                isError = true
                errorMessage = e.message.toString()
            }
        } else {
            isLoading = false
        }
    }

    MaterialTheme(
        colors = CustomTheme.psycheColors()
    ) {
        CompositionLocalProvider(
            LocalAuthToken provides authToken,
            LocalUser provides userState,
            LocalGroup provides groupState
        ) {
            Scaffold(
                topBar = {
                    Box(
                        modifier = Modifier
                            .clickable { currentScreen = "main" } // Make the entire TopAppBar clickable
                            .fillMaxWidth()
                    ) {
                        TopAppBar(
                            title = {
                                Text(
                                    text = stringResource(Res.string.home),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            },
                            navigationIcon = {
                                val icon: Painter = painterResource(Res.drawable.psyche_assistant_icon)
                                Icon(
                                    painter = icon,
                                    contentDescription = stringResource(Res.string.home),
                                    tint = Color.Unspecified
                                )
                            },
                        )
                    }
                },
                bottomBar = {
                    BottomNavigation {
                        BottomNavigationItem(
                            icon = { Icon(Icons.Filled.List, contentDescription = null) },
                            label = {
                                Text(
                                    stringResource(Res.string.survey),
                                    style = TextStyle(fontSize = 11.sp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            selected = currentScreen == "survey",
                            onClick = { currentScreen = "survey" }
                        )

                        BottomNavigationItem(
                            icon = { Icon(Icons.TwoTone.CheckCircle, contentDescription = null) },
                            label = {
                                Text(
                                    stringResource(Res.string.activities),
                                    style = TextStyle(fontSize = 11.sp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            selected = currentScreen == "activities",
                            onClick = { currentScreen = "activities" }
                        )
                        BottomNavigationItem(
                            icon = { Icon(Icons.TwoTone.Settings, contentDescription = null) },
                            label = {
                                Text(
                                    stringResource(Res.string.settings),
                                    style = TextStyle(fontSize = 11.sp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            selected = currentScreen == "settings",
                            onClick = { currentScreen = "settings" }
                        )

                    }
                },
                content = { innerPadding ->
                    if (isLoading) {
                        LoadingScreen()
                    } else {
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                        ) {
                            when (currentScreen) {
                                "main" -> MainPage()
                                "survey" -> SurveyPage(onBack = { currentScreen = "main" })
                                "settings" -> AccountManagementPage()
                                "activities" -> ActivityPage()
                            }
                        }
                    }
                }
            )

            // Error dialog
            if (isError) {
                AlertDialog(
                    onDismissRequest = { isError = false }, // Hide the dialog when the user clicks outside
                    title = { Text(stringResource(Res.string.no_connection)) },
                    text = { Text(errorMessage) },
                    confirmButton = {
                        Button(
                            onClick = { isError = false } // Hide the dialog when the user clicks "OK"
                        ) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}
