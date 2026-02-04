# 🔐 Passwordless Auth Flow (Email + OTP)

A robust, local-first Android application demonstrating passwordless authentication using Email + OTP, built with modern Android development practices.

## 🚀 Features

*   **Email-based Login:** User-friendly email entry with basic validation.
*   **Secure OTP Logic:** Local 6-digit OTP generation with a 60-second expiry window.
*   **Attempt Limiting:** Maximum of 3 attempts per OTP to prevent brute-forcing.
*   **Live Session Tracking:** A real-time timer that persists through screen rotations.
*   **Event Logging:** Integration with Timber to track authentication lifecycles.

## 🛠 Tech Stack

*   **Language:** Kotlin
*   **UI Framework:** Jetpack Compose
*   **Architecture:** MVVM (ViewModel + UI State)
*   **Async Operations:** Kotlin Coroutines & Flow
*   **Logging SDK:** Timber

## 📥 Setup & Installation

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/rmndep/passwordlessauth.git
    ```
2.  **Open in Android Studio:**
    File > Open > Select the project folder.
3.  **Sync Gradle:**
    Wait for the project to download dependencies and sync.
4.  **Run:**
    Select a Virtual Device (Emulator) or a physical device and press the Run button.
5.  **View OTP:**
    *   Open the **Logcat** tab in Android Studio.
    *   Filter by the tag `AuthEvent` to see the generated OTP code.

## 🧠 Technical Documentation

### 1. OTP Logic and Expiry Handling
The OTP logic is encapsulated in the `OtpManager` class.
*   **Generation:** Uses `(100000..999999).random()` to ensure a 6-digit integer.
*   **Expiry:** When a code is generated, a timestamp is set for $current\_time + 60,000ms$.
*   **Validation:** During verification, the current system time is compared against the stored timestamp. If the time has passed, a `ValidationResult.Expired` state is returned.

### 2. Data Structures Used
I utilized a `MutableMap<String, OtpData>` to store authentication states.
*   **Why?** A Map allows for $O(1)$ time complexity for lookups. By using the email as the key, the app can theoretically handle multiple user login attempts on the same device without state collision. It ensures that generating a new OTP for a specific email automatically overwrites and invalidates the previous entry.

### 3. External SDK Choice
I integrated **Timber** for logging.
*   **Why?** Unlike the standard `android.util.Log`, Timber prevents log leaks in production builds (via custom Trees) and provides a cleaner API. It was used to log the four mandatory events:
    *   `OTP_GENERATED`
    *   `OTP_VALIDATION_SUCCESS`
    *   `OTP_VALIDATION_FAILURE`
    *   `LOGOUT`

## AI Usage Disclosure : gpt used for debugging purpose and writing sample code structure.

## 📂 Project Structure

```plaintext
ui/             # Compose Screens and UI Logic
viewmodel/      # AuthViewModel and AuthUiState
data/           # OtpManager and Data Models
analytics/      # Timber SDK Initialization and Logging
```
