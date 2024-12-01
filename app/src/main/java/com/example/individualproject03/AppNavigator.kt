package com.example.individualproject03

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.individualproject03.ui.GameScreen
import com.example.individualproject03.ui.LoginScreen
import com.example.individualproject03.ui.SplashScreen
import com.example.individualproject03.ui.HomeScreen
import com.example.individualproject03.ui.RegistrationScreen

@Composable
fun AppNavigator() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("gameScreen") { GameScreen(navController) }
        composable("registration") { RegistrationScreen(navController) }
    }
}