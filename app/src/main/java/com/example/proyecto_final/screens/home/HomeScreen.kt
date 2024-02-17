package com.example.proyecto_final.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.proyecto_final.Items.Items_menu_lateral
import com.example.proyecto_final.model.Sesion
import com.example.proyecto_final.navigation.rutaActual
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun Home(navController: NavController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val viewModel: HomeScreenViewModel = viewModel()

    rememberCoroutineScope()
    val locationPermissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )


    LaunchedEffect(locationPermissionState) {
        locationPermissionState.launchPermissionRequest()
    }

    PermissionRequired(
        permissionState = locationPermissionState,
        permissionNotGrantedContent = {

            Text(" ")
        },
        permissionNotAvailableContent = {

        }
    ) {
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

                LaunchedEffect(Unit) {
                    viewModel.leerDatos()
                }

                val sesionesUiState by viewModel.sesiones.collectAsState()
                Log.d(
                    "Contenido",
                    "Número de sesiones en el estado: ${sesionesUiState.sessionsList.size}"
                )

                if (sesionesUiState.sessionsList.isNotEmpty()) {
                    if (sesionesUiState.isShowingDetailsPage) {
                        // Página de detalles
                        SesionesDetail(sesion = sesionesUiState.currentSession) {
                            viewModel.navigateBackToListPage()
                        }
                    } else {
                        // Lista de sesiones
                        LazyColumn {
                            items(sesionesUiState.sessionsList) { sesion ->
                                SesionesCard(sesion = sesion) {
                                    viewModel.updateCurrentSession(sesion)
                                    viewModel.navigateToDetailsPage()
                                }
                            }
                        }
                    }
                } else {
                    Log.d("Sesiones", "La lista de sesiones está vacía")
                }


            }
        }
    }
}

@Composable
fun SesionesCard(sesion: Sesion, onClick: () -> Unit) {
    Log.d("SesionesCard", "Mostrando tarjeta para sesión: $sesion")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onClick()
            },
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

@Composable
fun SesionesDetail(sesion: Sesion, onBackClick: () -> Unit) {
    val cameraPositionState = rememberCameraPositionState{
        CameraPosition(LatLng(0.0, 0.0),13f, 0f, 0f)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onBackClick,
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            Text("Volver a la lista")
        }

        val painter: Painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(data = sesion.img).apply(block = fun ImageRequest.Builder.() {
                crossfade(true)
            }).build()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .height(400.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .padding(bottom = 16.dp)
            )
        }
        Text(
            text = "Título: ${sesion.titulo}",
            modifier = Modifier
                .padding(top = 16.dp)
        )
        Text(
            text = "Cine: ${sesion.cine}",
            modifier = Modifier
                .padding(top = 16.dp)
        )
        Text(
            text = "Sesion: ${sesion.sesion}",
            modifier = Modifier
                .padding(top = 16.dp)
        )
        val latLng = LatLng(sesion.latitud!!, sesion.longitud!!)
        cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 13f)
        val latLngState = rememberMarkerState(position = latLng)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
        ){
            GoogleMap(
                modifier = Modifier,
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings()
            ){
                Marker(state = latLngState)
            }
        }
    }
}