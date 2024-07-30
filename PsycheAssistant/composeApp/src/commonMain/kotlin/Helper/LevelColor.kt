package org.psyche.assistant.Helper

import androidx.compose.ui.graphics.Color
import org.psyche.assistant.Model.SurveyModel
import org.psyche.assistant.Survey

/**
 * Store hard-coded colors in separate model,
 * just to avoid interfering with UI models, but ensuring uniformity in text color across platforms.
 */
object LevelColor {
    val blue = Color(0xFF2196F3)
    val green = Color(0xFF4CAF50)
    val yellow = Color(0xFFFFEB3B)
    val red = Color(0xFFF44336)

    fun getColor(score: Double): Color {
        when {
            score <= 10 -> return blue
            score <= 15 -> return green
            score <= 20 -> return yellow
            else -> return red
        }

    }
}