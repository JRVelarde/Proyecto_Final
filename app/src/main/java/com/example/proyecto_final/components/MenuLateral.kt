package com.example.proyecto_final.components

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import com.example.proyecto_final.Items.Items_menu_lateral.Item_menu_lateral1
import com.example.proyecto_final.Items.Items_menu_lateral.Item_menu_lateral2
import com.example.proyecto_final.Items.Items_menu_lateral.Item_menu_lateral3
import com.example.proyecto_final.navigation.rutaActual
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuLateral(
    navController: NavHostController,
    drawerState: DrawerState,
    contenido: @Composable () -> Unit
){
    val scope = rememberCoroutineScope()
    val menu_items = listOf(
        Item_menu_lateral1,
        Item_menu_lateral2,
        Item_menu_lateral3
    )
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                menu_items.forEach {item->
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
                            navController.navigate(item.ruta)
                        }
                    )
                }
            }
        }
    ) {
        contenido()
    }
}