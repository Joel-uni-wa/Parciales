package com.example.mygamecolor.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mygamecolor.ui.screens.GameScreen
import com.example.mygamecolor.ui.screens.ResultScreen
import com.example.mygamecolor.ui.screens.WelcomeScreen
import com.example.mygamecolor.viewmodel.GameViewModel

object Routes {
    const val WELCOME = "welcome"
    const val GAME = "game"
    const val RESULT = "result"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: GameViewModel = viewModel()

    NavHost(navController = navController, startDestination = Routes.WELCOME) {
        composable(Routes.WELCOME) {
            WelcomeScreen(
                onStartGame = { navController.navigate(Routes.GAME) }
            )
        }
        composable(Routes.GAME) {
            GameScreen(
                viewModel = viewModel,
                onGameFinished = {
                    navController.navigate(Routes.RESULT) {
                        popUpTo(Routes.GAME) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.RESULT) {
            ResultScreen(
                viewModel = viewModel,
                onPlayAgain = {
                    navController.navigate(Routes.GAME) {
                        popUpTo(Routes.WELCOME) { inclusive = false }
                    }
                }
            )
        }
    }
}