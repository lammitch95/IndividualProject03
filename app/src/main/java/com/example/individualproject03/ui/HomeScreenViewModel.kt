package com.example.individualproject03.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.individualproject03.model.User
import com.example.individualproject03.utils.GameStats
import com.example.individualproject03.utils.LoggedUser

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