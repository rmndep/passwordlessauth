package com.example.passwordlessauth.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordlessauth.analytics.AnalyticsLogger
import com.example.passwordlessauth.data.OtpManager
import com.example.passwordlessauth.data.ValidationResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val email: String = "",
    val otpInput: String = "",
    val isOtpSent: Boolean = false,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false,
    val sessionStartTime: Long = 0L
)

class AuthViewModel : ViewModel() {
    private val otpManager = OtpManager()

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _uiState.value = _uiState.value.copy(email = newEmail, errorMessage = null)
    }

    fun onOtpChange(newOtp: String) {
        _uiState.value = _uiState.value.copy(otpInput = newOtp, errorMessage = null)
    }

    fun sendOtp() {
        val code = otpManager.generateOtp(_uiState.value.email)
        _uiState.value = _uiState.value.copy(isOtpSent = true, otpInput = "", errorMessage = null)
        AnalyticsLogger.logEvent("OTP_GENERATED", "Email: ${_uiState.value.email}, Code: $code (Dev Only)")
    }

    fun verifyOtp() {
        val result = otpManager.validateOtp(_uiState.value.email, _uiState.value.otpInput)
        when (result) {
            ValidationResult.Success -> {
                _uiState.value = _uiState.value.copy(isLoggedIn = true, sessionStartTime = System.currentTimeMillis())
                AnalyticsLogger.logEvent("OTP_VALIDATION_SUCCESS")
            }
            ValidationResult.Invalid -> {
                _uiState.value = _uiState.value.copy(errorMessage = "Invalid OTP")
                AnalyticsLogger.logEvent("OTP_VALIDATION_FAILURE", "Reason: Wrong Code")
            }
            ValidationResult.Expired -> {
                _uiState.value = _uiState.value.copy(errorMessage = "OTP Expired. Please resend.")
                AnalyticsLogger.logEvent("OTP_VALIDATION_FAILURE", "Reason: Expired")
            }
            ValidationResult.MaxAttemptsExceeded -> {
                _uiState.value = _uiState.value.copy(errorMessage = "Max attempts reached. Generate a new OTP.")
                AnalyticsLogger.logEvent("OTP_VALIDATION_FAILURE", "Reason: Max Attempts")
            }
        }
    }

    fun logout() {
        _uiState.value = AuthUiState()
        AnalyticsLogger.logEvent("LOGOUT")
    }
}