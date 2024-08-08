package org.psyche.assistant.Storage

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.psyche.assistant.PsycheAssistantDB
import org.psyche.assistant.appContext

actual fun create(): PsycheAssistantDB {
    val driver = AndroidSqliteDriver(PsycheAssistantDB.Schema, appContext, "PsycheAssistantDB")
    return PsycheAssistantDB(driver)
}