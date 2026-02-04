package com.example.passwordlessauth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.passwordlessauth.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun AuthApp(viewModel: AuthViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!uiState.isLoggedIn) {
                if (!uiState.isOtpSent) {
                    EmailInputScreen(uiState.email, viewModel::onEmailChange, viewModel::sendOtp)
                } else {
                    OtpInputScreen(uiState.otpInput, uiState.errorMessage, viewModel::onOtpChange, viewModel::verifyOtp, viewModel::sendOtp)
                }
            } else {
                SessionScreen(uiState.sessionStartTime, viewModel::logout)
            }
        }
    }
}

@Composable
fun EmailInputScreen(email: String, onEmailChange: (String) -> Unit, onSend: () -> Unit) {
    TextField(value = email, onValueChange = onEmailChange, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
    Spacer(modifier = Modifier.height(16.dp))
    Button(onClick = onSend, enabled = email.contains("@"), modifier = Modifier.fillMaxWidth()) {
        Text("Send OTP")
    }
}

@Composable
fun OtpInputScreen(otp: String, error: String?, onOtpChange: (String) -> Unit, onVerify: () -> Unit, onResend: () -> Unit) {
    TextField(value = otp, onValueChange = onOtpChange, label = { Text("6-Digit OTP") }, modifier = Modifier.fillMaxWidth())
    if (error != null) Text(error, color = Color.Red, style = MaterialTheme.typography.bodySmall)
    Spacer(modifier = Modifier.height(16.dp))
    Button(onClick = onVerify, modifier = Modifier.fillMaxWidth()) { Text("Login") }
    TextButton(onClick = onResend) { Text("Resend OTP") }
}

@Composable
fun SessionScreen(startTime: Long, onLogout: () -> Unit) {
    var ticks by rememberSaveable { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        while (true) {
            ticks = (System.currentTimeMillis() - startTime) / 1000
            delay(1000)
        }
    }

    val minutes = (ticks / 60).toString().padStart(2, '0')
    val seconds = (ticks % 60).toString().padStart(2, '0')

    Text("Session Active", style = MaterialTheme.typography.headlineMedium)
    Text("Started at: ${java.util.Date(startTime)}")
    Text("Duration: $minutes:$seconds", style = MaterialTheme.typography.displaySmall)
    Spacer(modifier = Modifier.height(24.dp))
    Button(onClick = onLogout) { Text("Logout") }
}