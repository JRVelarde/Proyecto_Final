package com.example.proyecto_final.screens.mapa

import android.location.Location
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyecto_final.Items.Items_menu_lateral
import com.example.proyecto_final.navigation.rutaActual
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Mapas(
    navController: NavController
){
    val locationService = LocationService()
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
                    .fillMaxSize(),
                locationService = locationService
            )

    }

}
@Composable
fun MyGoogleMaps(modifier: Modifier = Modifier, locationService: LocationService) {
    val context = LocalContext.current

    var userLocation by remember { mutableStateOf<Location?>(null) }

    LaunchedEffect(locationService) {
        // Llamada suspendida dentro de LaunchedEffect para evitar el error
        userLocation = locationService.getUserLocation(context)
    }

    val cameraPositionState = rememberCameraPositionState {}

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        onMapLoaded = {
            // Puedes realizar acciones cuando el mapa se haya cargado si es necesario
        },
        contentPadding = PaddingValues(0.dp)
    ) {
        userLocation?.let {
            // Añadir un marcador en la ubicación del usuario
            Marker(
                state = rememberMarkerState(position = LatLng(it.latitude, it.longitude)),
                title = "Tu ubicación",
                onClick = {
                    // Acciones al hacer clic en el marcador
                    true // Devuelve true para indicar que el clic en el marcador ha sido manejado
                }
            )
        }
    }
}




