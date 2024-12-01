package com.example.individualproject03.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

/**
 * Composable function for the Home Screen.
 *
 * - Displays a personalized welcome message based on the logged-in user's first name.
 * - Allows the user to select and navigate to game modes ("Easy" or "Hard").
 * - Shows progress tracking for each game mode.
 *
 * - Uses `NavHostController` to navigate to the game screen upon selecting a game mode.
 *
 * - Personalized greeting.
 * - Interactive game mode selection.
 * - Progress indication for completed levels.
 */

@Composable
fun HomeScreen(navHostController: NavHostController, homeScreenViewModel: HomeScreenViewModel = viewModel()){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(201, 1, 254),
                        Color(0, 76, 249)
                    )
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Welcome ${homeScreenViewModel.userFirstName.value}!",
            color = Color.White,
            style = TextStyle(fontSize = 40.sp),
        )

        Spacer(modifier = Modifier.padding(10.dp))

        Button(
            onClick = {
                homeScreenViewModel.chosenGameMode("Easy")
                navHostController.navigate("gameScreen")
            },
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 50.dp,
                    shape = RoundedCornerShape(4.dp),
                    ambientColor = Color.Green,
                    spotColor = Color.Green
                ),
            colors = ButtonDefaults.buttonColors(containerColor = Color(20, 255, 0, 255)),
        ){
            Text(
                "Easy",
                color = Color.Black,
                style = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Bold)
            )
        }

        Text(
            "Completed: ${homeScreenViewModel.easyModeProgress.value}/3",
            color = Color.Green,
            style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(vertical = 5.dp)
        )

        Spacer(modifier = Modifier.padding(10.dp))


        Button(
            onClick = {
                homeScreenViewModel.chosenGameMode("Hard")
                navHostController.navigate("gameScreen")

            },
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 50.dp,
                    shape = RoundedCornerShape(4.dp),
                    ambientColor = Color.Green,
                    spotColor = Color.Green
                ),
            colors = ButtonDefaults.buttonColors(containerColor = Color(255, 0, 0, 255)),
        ){
            Text(
                "Hard",
                color = Color.Black,
                style = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Bold),
            )
        }

        Text(
            "Completed: ${homeScreenViewModel.hardModeProgress.value}/3",
            color = Color.Red,
            style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(vertical = 5.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen(){
    HomeScreen(navHostController = rememberNavController())
}

