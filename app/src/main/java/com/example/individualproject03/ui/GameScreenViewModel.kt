package com.example.individualproject03.ui

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.individualproject03.data.database.AppDatabase
import com.example.individualproject03.model.Level
import com.example.individualproject03.model.LevelRepository
import com.example.individualproject03.utils.GameStats
import com.example.individualproject03.utils.LoggedUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel for the Game Screen.
 *
 * - Manages the state and logic for the game screen.
 * - Tracks the user's progress and interactions within a specific game mode (Easy or Hard).
 * - Loads and updates level data, including the grid structure and start/end tiles.
 * - Handles user input for updating direction sequences in the grid.
 * - Synchronizes user progress with the database.
 *
 * - **Dynamic Grid Management**: Configurable grid dimensions for level layout.
 * - **Direction Sequence Tracking**: Tracks arrows and user-defined directions in the grid.
 * - **Level Progression**: Dynamically loads levels based on user progress and mode.
 * - **Database Synchronization**: Updates user progress in the database.
 *
 * - Loads user details to determine the current mode and progress.
 * - Fetches the level map and sets the start and end tiles.
 *
 * - `setSelectedArrowIndex`: Updates the index of the selected arrow.
 * - `setDroppedBoxIndex`: Updates the index where the box is dropped.
 * - `updateDirection`: Updates the direction sequence based on user interaction.
 * - `updateModeProgress`: Updates progress and synchronizes it with the database.
 * - `resetLevel`: Placeholder for resetting the current level.
 *
 * - **AppDatabase**: Provides DAO for updating user progress.
 * - **LevelRepository**: Fetches level data for the specified mode and progress.
 * - **LoggedUser**: Tracks the logged-in user's details and progress.
 */


data class Tile(
    val gridX: Int,
    val gridY: Int,
    val screenX: Float,
    val screenY: Float
)

class GameScreenViewModel(private val appDatabase: AppDatabase): ViewModel() {

    companion object {
        fun provideFactory(appDatabase: AppDatabase): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(GameScreenViewModel::class.java)) {
                        return GameScreenViewModel(appDatabase) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }

    val gridWidth = mutableIntStateOf(10)
    val gridHeight = mutableIntStateOf(16)

    var currentMode = mutableStateOf("Easy")
        private set
    var currentModeProgress = mutableStateOf(1)
    var directionSequence = mutableStateOf(mutableListOf("", "", "", "", ""))

    val initialDirections = listOf("left","up","right","down")

    var selectedArrowIndex = mutableStateOf(0)
        private set
    var droppedBoxIndex = mutableStateOf(0)
        private set

    var levelMapState = mutableStateOf<Level?>(null)
    val startTileState = mutableStateOf<Pair<Int, Int>?>(null)
    val endTileState = mutableStateOf<Pair<Int, Int>?>(null)


    fun setSelectedArrowIndex(value: Int){
        selectedArrowIndex.value = value
    }

    fun setDroppedBoxIndex(value: Int){
        droppedBoxIndex.value = value
    }

    init {
        loadUserDetails()
        loadLevelMap()
    }

    private fun loadUserDetails() {
        val user = LoggedUser.loggedInUser
        if (user != null) {
            currentMode.value = GameStats.selectedMode
            if(currentMode.value == "Easy"){
                currentModeProgress.value = if (user.easyModeProgress == 0) 1 else user.easyModeProgress
            }
            if(currentMode.value == "Hard"){
                currentModeProgress.value = if(user.hardModeProgress == 0) 1 else user.hardModeProgress
            }

        }
    }

    private fun loadLevelMap(){
        val currentLevel = LevelRepository.getLevel(currentMode.value, currentModeProgress.value)
        levelMapState.value = currentLevel

        if (currentLevel != null) {
            startTileState.value = currentLevel.startTile
            endTileState.value = currentLevel.endTile
        }
    }

    fun updateDirection() {
            val updatedList = directionSequence.value.toMutableList()
            updatedList[droppedBoxIndex.value] = initialDirections[selectedArrowIndex.value]
            directionSequence.value = updatedList
    }

    fun updateModeProgress(newProgress: Int) {
        if(currentMode.value == "Easy"){
            LoggedUser.setEasyModeCount(newProgress)

        }
        if(currentMode.value == "Hard"){
            LoggedUser.setHardModeCount(newProgress)

        }

        viewModelScope.launch {
            try {
                val user = LoggedUser.loggedInUser
                if (user != null) {
                    if (currentMode.value == "Easy") {
                        appDatabase.userDao().updateEasyModeProgress(user.email, newProgress)
                    } else if (currentMode.value == "Hard") {
                        appDatabase.userDao().updateHardModeProgress(user.email, newProgress)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace() // Handle any exceptions
            }
        }

        currentModeProgress.value = newProgress
        directionSequence = mutableStateOf(mutableListOf("", "", "", "", ""))
        levelMapState = mutableStateOf<Level?>(null)
        loadUserDetails()
        loadLevelMap()
    }

    fun resetLevel(){

    }


}