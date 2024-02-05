package com.example.proyecto_final.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun rutaActual(navController: NavHostController): String? =
    navController.currentBackStackEntryAsState().value?.destination?.route