package org.psyche.assistant

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tencent.mmkv.MMKV
import org.psyche.assistant.Composable.Main.PsycheAssistantApp

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
