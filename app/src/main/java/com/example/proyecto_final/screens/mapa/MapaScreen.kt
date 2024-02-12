package com.example.proyecto_final.screens.mapa

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.proyecto_final.Items.Items_menu_lateral
import com.example.proyecto_final.navigation.rutaActual
import com.google.maps.android.compose.GoogleMap
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Mapas(
    navController: NavController
){

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed)

        val scope = rememberCoroutineScope()
        val menuItems = listOf(
            Items_menu_lateral.Item_menu_lateral4,
            Items_menu_lateral.Item_menu_lateral1,
            Items_menu_lateral.Item_menu_lateral2
        )
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Botón de apertura del menú lateral
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

            // Mapa
            MyGoogleMaps(
                modifier = Modifier
                    .fillMaxSize()
            )

    }

}
@Composable
fun MyGoogleMaps(modifier: Modifier = Modifier) {
    GoogleMap(
        modifier = modifier
    )
}

