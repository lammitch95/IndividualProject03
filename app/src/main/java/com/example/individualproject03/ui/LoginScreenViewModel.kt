package com.example.individualproject03.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.individualproject03.data.database.AppDatabase
import com.example.individualproject03.model.User
import com.example.individualproject03.utils.LoggedUser
import com.example.individualproject03.utils.ValidationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for managing the Login Screen state and logic.
 *
 * Handles user input, validation, and authentication:
 * - Manages mutable states for email, password, error messages, and password visibility.
 * - Validates user input using `ValidationUtils`.
 * - Authenticates users by verifying credentials with the database.
 * - Updates UI states and triggers success or error callbacks.
 *
 * Includes a factory method for creating an instance with `AppDatabase`.
 */


class LoginScreenViewModel(private val appDatabase: AppDatabase): ViewModel() {

    companion object {
        fun provideFactory(appDatabase: AppDatabase): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(LoginScreenViewModel::class.java)) {
                        return LoginScreenViewModel(appDatabase) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }

    var emailState = mutableStateOf("")
        private set
    var passwordState = mutableStateOf("")
        private set
    var passwordVisibility = mutableStateOf(false)
        private set
    var loginError = mutableStateOf("")
        private set
    fun onEmailChange(newEmail: String) {
        emailState.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        passwordState.value = newPassword
    }

    fun togglePasswordVisibility() {
        passwordVisibility.value = !passwordVisibility.value
    }

    fun login(onLoginSuccess: () -> Unit) {

        val emailError = ValidationUtils.validateEmail(emailState.value)
        val passwordError = ValidationUtils.validatePassword(passwordState.value)

        if (emailError.isEmpty() && passwordError.isEmpty()) {

            viewModelScope.launch(Dispatchers.IO) {
                val loginSuccess = appDatabase.userDao().login(emailState.value, passwordState.value)
                if (loginSuccess != null) {
                    withContext(Dispatchers.Main) {
                        loginError.value = ""
                       LoggedUser.setLoggedInUser(loginSuccess)
                        onLoginSuccess()
                    }

                } else {
                    withContext(Dispatchers.Main) {
                        loginError.value = "Login failed. Please check your credentials."
                    }

                }
            }
        } else {
            loginError.value = emailError.ifEmpty { passwordError }
        }


    }
}