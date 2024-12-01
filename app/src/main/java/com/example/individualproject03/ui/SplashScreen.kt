package com.example.individualproject03.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navHostController: NavHostController){
    LaunchedEffect(Unit) {
        delay(3000)
        navHostController.navigate("login")
    }

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
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Kodable",
            color = Color.White,
            style = TextStyle(fontSize = 75.sp, fontFamily = FontFamily.Monospace)

        )
        Text(
            "Programming Basics for Kids",
            color = Color.White,
            style = TextStyle(fontSize = 25.sp)

        )

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen(){
    SplashScreen(navHostController = rememberNavController())
}