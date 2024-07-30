package org.psyche.assistant.Helper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.intl.Locale
import org.jetbrains.compose.resources.StringResource
import psycheassistant.composeapp.generated.resources.Res

/**
 * Ensures that initial setup of question language is equivalent to Locale.current.language, by storing it as a mutable.
 * TO-DO: Extend so the translation logic doesn't just effect JSON, but also XML resources.
 */
object GlobalLocaleState {
    var locale by mutableStateOf(Locale.current.language)
}