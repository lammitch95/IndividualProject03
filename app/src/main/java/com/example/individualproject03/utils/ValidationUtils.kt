package com.example.individualproject03.utils

import com.example.individualproject03.model.User

object ValidationUtils {

    // Validates email format
    fun validateEmail(email: String): String {
        return if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Invalid email format"
        } else ""
    }

    // Validates password with basic rules
    fun validatePassword(password: String): String {
        return if (password.length < 8) {
            "Password must be at least 8 characters"
        } else ""
    }
}

object LoggedUser {
    var loggedInUser: User? = null
        private set

    fun setLoggedInUser(value: User?){
        loggedInUser = value
    }


    fun setEasyModeCount(value: Int){
        loggedInUser?.let {
            it.easyModeProgress = value
        }
    }

    fun setHardModeCount(value: Int){
        loggedInUser?.let {
            it.hardModeProgress = value
        }
    }


}

object GameStats {
    var selectedMode: String = ""
        private set

    fun setSelectedMode(value: String){
        selectedMode = value
    }
}