package com.example.proyecto_final.screens.mapa

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.proyecto_final.Items.Items_menu_lateral
import com.example.proyecto_final.navigation.rutaActual
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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
    val mapView = MapView(LocalContext.current)
    remember (mapView) {
        mapView.apply {
            onCreate(null)
            onResume()
        }
    }
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
        val context = LocalContext.current
        val updatedContext = rememberUpdatedState(context)

        val locationPermissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) { results ->
            if (results.containsValue(true)) {
                // Permiso concedido, muestra el mapa con la ubicación
                mostrarMapa(mapView)
            } else {
                // Permiso denegado, muestra un mensaje al usuario
                mostrarMensajePermisoDenegado(updatedContext.value)
            }
        }
        locationPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
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

@SuppressLint("MissingPermission")
@Composable
private fun mostrarMapa(mapView: MapView) {
    val context = LocalContext.current
    mapView.getMapAsync { googleMap ->
        onMapReady(googleMap, context)
    }
}

@Composable
private fun onMapReady(googleMap: GoogleMap, context: Context) {
    // Obtén la ubicación actual del usuario (requiere permiso)
    val location = getLocation()

    // Mueve la cámara y añade un marcador en la ubicación del usuario
    if (location != null) {
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 15f)
        )
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(location.latitude, location.longitude))
                .title("Tú estás aquí")
        )
    }
}


@Composable
@SuppressLint("MissingPermission")
private fun getLocation(): Location? {
    val locationManager = LocalContext.current.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
}
private fun mostrarMensajePermisoDenegado(context: Context) {
    AlertDialog.Builder(context)
        .setTitle("Permiso denegado")
        .setMessage("Es necesario conceder el permiso de ubicación para usar el mapa.")
        .setPositiveButton("Aceptar", null)
        .show()
}

