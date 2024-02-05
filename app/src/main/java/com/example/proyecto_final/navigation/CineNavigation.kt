package com.example.proyecto_final.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_final.screens.CineSplashScreen
import com.example.proyecto_final.screens.home.Home
import com.example.proyecto_final.screens.login.LogInScreen
import com.example.proyecto_final.screens.mapa.Mapas
import com.example.proyecto_final.screens.sesiones.Sesiones
import com.example.proyecto_final.screens.usuario.Perfiles

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
            Home()
        }
        composable(CineScreens.PerfilScreen.name){
            Perfiles(navController = navController)
        }
        composable(CineScreens.MapaScreen.name){
            Mapas(navController = navController)
        }
        composable(CineScreens.SesionesScreen.name){
            Sesiones(navController = navController)
        }

    }
}