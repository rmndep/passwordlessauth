package com.example.passwordlessauth.analytics

import timber.log.Timber

object AnalyticsLogger {
    fun logEvent(event: String, details: String = "") {
        Timber.tag("AuthEvent").d("$event: $details")
    }

    fun init() {
        Timber.plant(Timber.DebugTree())
    }
}