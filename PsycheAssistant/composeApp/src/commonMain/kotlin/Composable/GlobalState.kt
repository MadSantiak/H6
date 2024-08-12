package org.psyche.assistant.Composable

import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import org.psyche.assistant.Model.Group.Group
import org.psyche.assistant.Model.User.User

val LocalAuthToken = compositionLocalOf<MutableState<String?>> { error("No token found..") }
val LocalUser = compositionLocalOf<MutableState<User?>> { error("No user found..") }
val LocalGroup = compositionLocalOf<MutableState<Group?>> { error("No group found..") }