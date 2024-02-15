package com.example.proyecto_final.screens.usuario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyecto_final.Items.Items_menu_lateral
import com.example.proyecto_final.model.User
import com.example.proyecto_final.navigation.CineScreens
import com.example.proyecto_final.navigation.rutaActual
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch


private val auth: FirebaseAuth = Firebase.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Perfiles(
    navController: NavController,
) {
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                val scope = rememberCoroutineScope()
                val menuItems = listOf(
                    Items_menu_lateral.Item_menu_lateral4,
                    Items_menu_lateral.Item_menu_lateral3
                )

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
                            navController.navigate(item.ruta)
                        }
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Cuenta")
                },
                navigationIcon = {
                    val scope = rememberCoroutineScope()
                    IconButton(onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }) {
                        Icon(Icons.Outlined.Menu, "Abrir Menú Lateral")
                    }
                }
            )
            val currentUser = getCurrentUser()

            if (currentUser != null) {
                // Mostrar información del usuario
                Text(text = "Correo Electrónico: ${currentUser.email}")
                Text(text = "Nombre de Usuario: ${currentUser.displayName}")
            } else {
                // Manejar el caso en que no hay usuario autenticado
                Text(text = "No hay usuario autenticado")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Cerrar sesión directamente
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(CineScreens.LoginScreen.name)
                    // Puedes navegar a otra pantalla o realizar otras acciones después de cerrar sesión
                    // navController.navigate("otra_ruta")
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Cerrar Sesión")
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

fun getCurrentUser(): User? {
    val currentUser = auth.currentUser

    return if (currentUser != null) {
        // Obtener información del usuario actual
        val userId = currentUser.uid
        val email = currentUser.email
        val displayName = email?.split("@")?.get(0)

        // Crear un objeto User con la información obtenida
        displayName?.let {
            User(
                userId = userId,
                email = email,
                displayName = it,
                id = null
            )
        }
    } else {
        null // No hay usuario autenticado
    }
}

