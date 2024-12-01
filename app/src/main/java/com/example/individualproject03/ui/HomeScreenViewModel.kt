package com.example.individualproject03.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.individualproject03.model.User
import com.example.individualproject03.utils.GameStats
import com.example.individualproject03.utils.LoggedUser

/**
 * ViewModel for the Home Screen.
 * - Manages user-specific data such as first name and game progress.
 * - Loads logged-in user details to display on the Home Screen.
 * - Allows the selection of a game mode (easy/hard) via `GameStats`.
**/
class HomeScreenViewModel(): ViewModel(){

    var userFirstName = mutableStateOf("Guest")
        private set
    var easyModeProgress = mutableStateOf(0)
        private set
    var hardModeProgress = mutableStateOf(0)
        private set

    init {
        loadUserDetails()
    }

    private fun loadUserDetails() {
        val user = LoggedUser.loggedInUser
        if (user != null) {
            userFirstName.value = user.firstName
            easyModeProgress.value = user.easyModeProgress
            hardModeProgress.value = user.hardModeProgress
        } else {
            userFirstName.value = "Guest"
        }
    }

    fun chosenGameMode(value: String){
        GameStats.setSelectedMode(value)
    }


}