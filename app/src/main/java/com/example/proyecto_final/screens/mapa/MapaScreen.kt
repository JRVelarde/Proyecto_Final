package com.example.proyecto_final.screens.mapa

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.example.proyecto_final.Items.Items_menu_lateral
import com.example.proyecto_final.navigation.rutaActual
import kotlinx.coroutines.launch





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Mapas(
    navController: NavController
){

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed)
    MenuLateral(
        navController = navController,
        drawerState = drawerState
    ) {
        Contenido(
            navController = navController,
            drawerState = drawerState
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Contenido(
    navController: NavController,
    drawerState: DrawerState
) {
    val scope = rememberCoroutineScope()

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(text = "Mapas")},
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }) {
                        Icon(Icons.Outlined.Menu, "Abrir Menú Lateral")
                    }
                }
            )
        }
    ){

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuLateral(
    navController: NavController,
    drawerState: DrawerState,
    contenido: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val menuItems = listOf(
        Items_menu_lateral.Item_menu_lateral4,
        Items_menu_lateral.Item_menu_lateral1,
        Items_menu_lateral.Item_menu_lateral2
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                menuItems.forEach { item ->
                    NavigationDrawerItem(
                        icon = {
                            Icon(item.icon, contentDescription = null)
                        },
                        label = { Text(text = item.title) },
                        selected = rutaActual(navController) == item.ruta,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }
                            navController.navigate(item.ruta) // Usa el NavHostController principal
                        }
                    )
                }
            }
        }
    ) {
        contenido()
    }
}

