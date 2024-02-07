package com.example.proyecto_final.Items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.proyecto_final.navigation.CineScreens

sealed class Items_menu_lateral(
    val icon: ImageVector,
    val title: String,
    val ruta: String
){
    object Item_menu_lateral1 : Items_menu_lateral(
        Icons.Outlined.AccountBox,
        "Cuenta",
        CineScreens.PerfilScreen.name
    )
    object Item_menu_lateral2 : Items_menu_lateral(
        Icons.Outlined.Movie,
        "Sesiones cerca",
        CineScreens.SesionesScreen.name
    )
    object Item_menu_lateral3 : Items_menu_lateral(
        Icons.Outlined.Map,
        "Mapa",
        CineScreens.MapaScreen.name
    )

    object Item_menu_lateral4 : Items_menu_lateral(
        Icons.Outlined.Home,
        "Inicio",
        CineScreens.CineHomeScreen.name
    )
}