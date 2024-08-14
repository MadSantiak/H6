package org.psyche.assistant

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tencent.mmkv.MMKV
import org.psyche.assistant.Composable.Layout.PsycheAssistantApp

import org.psyche.assistant.Model.SurveyRepository
import java.sql.Driver

lateinit var appContext: Context

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appContext = applicationContext
        MMKV.initialize(this)

        setContent {
            PsycheAssistantApp()
        }
    }
}
