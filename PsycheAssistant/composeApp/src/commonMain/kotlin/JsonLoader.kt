package org.psyche.assistant

import org.psyche.assistant.Model.QuestionModel

/**
 * Expect function for reading Questions from the stored JSON file.
 * NOTE! File MUST be in commonMain in order for KMP to recognize its expect (common)/actual (native) functionality.
 */
expect object JsonLoader {
    fun loadQuestions(locale: String): List<QuestionModel>
}