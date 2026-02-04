package com.example.passwordlessauth.data

import java.util.concurrent.TimeUnit

data class OtpData(
    val code: String,
    val expiryTime: Long,
    val attempts: Int = 0
)

class OtpManager {
    // Map of Email to OtpData
    private val otpStorage = mutableMapOf<String, OtpData>()

    fun generateOtp(email: String): String {
        val code = (100000..999999).random().toString()
        val expiry = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(60)
        otpStorage[email] = OtpData(code, expiry, 0)
        return code
    }

    fun validateOtp(email: String, inputCode: String): ValidationResult {
        val data = otpStorage[email] ?: return ValidationResult.Invalid

        if (System.currentTimeMillis() > data.expiryTime) {
            return ValidationResult.Expired
        }

        if (data.attempts >= 3) {
            return ValidationResult.MaxAttemptsExceeded
        }

        return if (data.code == inputCode) {
            otpStorage.remove(email) // Clear on success
            ValidationResult.Success
        } else {
            val updatedData = data.copy(attempts = data.attempts + 1)
            otpStorage[email] = updatedData
            ValidationResult.Invalid
        }
    }
}

sealed class ValidationResult {
    object Success : ValidationResult()
    object Invalid : ValidationResult()
    object Expired : ValidationResult()
    object MaxAttemptsExceeded : ValidationResult()
}