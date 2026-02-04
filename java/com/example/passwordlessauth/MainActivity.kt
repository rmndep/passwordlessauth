package com.example.passwordlessauth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.passwordlessauth.analytics.AnalyticsLogger
import com.example.passwordlessauth.ui.AuthApp
import com.example.passwordlessauth.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AnalyticsLogger.init()
        setContent {
            val viewModel: AuthViewModel = viewModel()
            AuthApp(viewModel)
        }
    }
}