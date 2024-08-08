package org.psyche.assistant.Storage

import org.psyche.assistant.PsycheAssistantDB

/**
 * Expect initlization SQL Delight database, called Actual by relevant native code.
 */
expect fun create(): PsycheAssistantDB

