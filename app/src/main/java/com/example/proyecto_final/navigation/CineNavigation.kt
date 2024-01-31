package com.example.proyecto_final.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_final.screens.CineSplashScreen
import com.example.proyecto_final.screens.home.Home
import com.example.proyecto_final.screens.login.LogInScreen

@Composable
fun CineNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = CineScreens.SplashScreen.name
    ){
        composable(CineScreens.SplashScreen.name){
            CineSplashScreen(navController = navController)
        }
        composable(CineScreens.LoginScreen.name){
            LogInScreen(navController = navController)
        }
        composable(CineScreens.CineHomeScreen.name){
            Home(navController = navController)
        }
    }
}