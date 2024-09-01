package org.psyche.assistant.Service

/**
 * Expect implementation for using platform-specific configuration file for fetching URL
 */
expect object ConfigProvider {
    val baseUrl: String
    val secondaryUrl: String?
    val tertiaryUrl: String?
}
