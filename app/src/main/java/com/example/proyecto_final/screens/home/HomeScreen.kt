package com.example.proyecto_final.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyecto_final.Items.Items_menu_lateral
import com.example.proyecto_final.model.Sesions
import com.example.proyecto_final.navigation.rutaActual
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun Home(navController: NavController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                val scope = rememberCoroutineScope()
                val menuItems = listOf(
                    Items_menu_lateral.Item_menu_lateral1,
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
                    Text(text = "Cines")
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

            Spacer(modifier = Modifier.height(16.dp))

            // Aquí puedes mostrar las sesiones en tu diseño
            val viewModel: HomeScreenViewModel = viewModel()

            LaunchedEffect(Unit){
                viewModel.leerDatos()
            }

            val sesiones by viewModel.sesiones.collectAsState()
            Log.d("Contenido", "Número de sesiones en el estado: ${sesiones.size}")

            if (sesiones.isNotEmpty()) {
                Log.d("Sesiones", "Data del documento: ${sesiones[1].cine}")

                LazyColumn {

                    items(sesiones) { sesion ->
                        SesionesCard(sesion = sesion)
                    }
                }
            } else {
                Log.d("Sesiones", "La lista de sesiones está vacía")
            }
        }
    }
}


@Composable
fun SesionesCard(sesion: Sesions) {
    Log.d("SesionesCard", "Mostrando tarjeta para sesión: $sesion")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Muestra la información de la sesión dentro de la tarjeta
            Text(text = "Cine: ${sesion.cine}", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Título: ${sesion.titulo}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Sesión: ${sesion.sesion}")
            // Puedes agregar más información según sea necesario
        }
    }
}


