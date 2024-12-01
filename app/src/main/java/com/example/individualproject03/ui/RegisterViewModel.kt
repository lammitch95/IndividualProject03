package com.example.individualproject03.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.individualproject03.data.database.AppDatabase
import com.example.individualproject03.model.User
import com.example.individualproject03.utils.ValidationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistrationScreenViewModel(private val appDatabase: AppDatabase): ViewModel(){

    companion object {
        fun provideFactory(appDatabase: AppDatabase): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(RegistrationScreenViewModel::class.java)) {
                        return RegistrationScreenViewModel(appDatabase) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }

    var firstNameState = mutableStateOf("")
        private set
    var lastNameState = mutableStateOf("")
        private set
    var emailState = mutableStateOf("")
        private set
    var passwordState = mutableStateOf("")
        private set
    var passwordVisiblity = mutableStateOf(false)
        private set
    var isLoading = mutableStateOf(false)
        private set
    var registrationError = mutableStateOf("")
        private set


    val firstNameError: String get() = if (firstNameState.value.isEmpty()) "First name cannot be empty" else ""
    val lastNameError: String get() = if (lastNameState.value.isEmpty()) "Last name cannot be empty" else ""
    val emailError: String get() = if (emailState.value.isEmpty()) "Email cannot be empty" else ValidationUtils.validateEmail(emailState.value)
    val passwordError: String get() = if (passwordState.value.isEmpty()) "Password cannot be empty" else ValidationUtils.validatePassword(passwordState.value)

    fun onFirstNameChange(value: String){ firstNameState.value = value }
    fun onLastNameChange(value: String){lastNameState.value = value}
    fun onEmailChange(value: String){emailState.value = value}
    fun onPasswordChange(value: String){passwordState.value = value}
    fun onPasswordVisibility(){passwordVisiblity.value = !passwordVisiblity.value}
    fun onIsLoadingChange(value: Boolean){isLoading.value = value}
    fun onRegistrationError(value: String){registrationError.value = value}

    fun registerUser(onRegisterSuccess: () -> Unit) {
        if (firstNameError.isEmpty() && lastNameError.isEmpty() && emailError.isEmpty() && passwordError.isEmpty()) {

            isLoading.value = true
            registrationError.value = ""

            viewModelScope.launch(Dispatchers.IO){
                try{
                    val existingUser = appDatabase.userDao().checkIfEmailExists(emailState.value)
                    if(existingUser !=  null){
                        withContext(Dispatchers.Main) {
                            isLoading.value = false
                            registrationError.value = "An account with this email already exists."
                        }
                        return@launch
                    }

                    val newUser = User(
                        firstName = firstNameState.value,
                        lastName = lastNameState.value,
                        email = emailState.value,
                        password = passwordState.value
                    )
                    appDatabase.userDao().insertUser(newUser)
                    withContext(Dispatchers.Main) {
                        isLoading.value = false
                        registrationError.value = ""
                        onRegisterSuccess()
                    }
                }catch(e: Exception){
                    withContext(Dispatchers.Main) {
                        isLoading.value = false
                        registrationError.value = "Registration failed. Please try again."
                    }
                    Log.e("RegistrationError", "Registration failed", e)
                }
            }

        } else {
            registrationError.value = "Please fill all fields correctly and accept the Terms & Conditions."
        }
    }

}