package com.example.proyecto_final.screens.mapa

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyecto_final.Items.Items_menu_lateral
import com.example.proyecto_final.navigation.rutaActual
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Mapas(
    navController: NavController
) {
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.background)
                    .clickable {
                        if (drawerState.isOpen) {
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    }
            ) {

                val menuItems = listOf(
                    Items_menu_lateral.Item_menu_lateral1,
                    Items_menu_lateral.Item_menu_lateral4
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
        gesturesEnabled = false,
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background)

    ){

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Mapas")
                },
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
            Spacer(modifier = Modifier.height(16.dp))
            MyGoogleMaps(
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun MyGoogleMaps(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val fusedLocationProvider = remember{
        LocationServices.getFusedLocationProviderClient(context)
    }
    val cameraPositionState = rememberCameraPositionState{
        CameraPosition(LatLng(0.0, 0.0),13f, 0f, 0f)
    }
    val YelmoIsla = LatLng(40.363979, -3.738369)
    val yelmoIslaMarkerState = rememberMarkerState(position = YelmoIsla)

    val Renoir = LatLng(40.424400, -3.713414)
    val renoirMarkerState = rememberMarkerState(position = Renoir)

    val CineCallao = LatLng(40.419983, -3.706109)
    val cineCallaoMarkerState = rememberMarkerState(position = CineCallao)

    val Dore = LatLng(40.411688, -3.699002)
    val doreMarkerState = rememberMarkerState(position = Dore)

    val Kinepolis = LatLng(40.393864,-3.796479)
    val kinepolisMarkerState = rememberMarkerState(position = Kinepolis)

    val CineEmbajadores = LatLng(40.400637, -3.698562)
    val cineEmbajadoresMarkerState = rememberMarkerState(position = CineEmbajadores)

    val GolemMadrid = LatLng(40.424618,-3.713605)
    val golemMadridMarkerState = rememberMarkerState(position = GolemMadrid)

    val YelmoCinesIdeal = LatLng(40.413665,-3.703888)
    val yelmoCinesIdealMarkerState = rememberMarkerState(position = YelmoCinesIdeal)

    val Vendi = LatLng(40.436569,-3.704077)
    val vendiMarkerState = rememberMarkerState(position = Vendi)

    val Capitol = LatLng(40.420512, -3.706678)
    val capitolMarkerState = rememberMarkerState(position = Capitol)

    LaunchedEffect(Unit){
        try {
            val location = fusedLocationProvider.lastLocation.await()
            location?.let{
                val latLng = LatLng(location.latitude, location.longitude)
                cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 13f)
            }
            Log.e("Location", location.toString())

        }catch (e: Exception){
            Log.e("MapScreen", "Error obtaining location: ${e.message}")

        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = true),
        uiSettings = MapUiSettings()
    ){
        Marker(state = yelmoIslaMarkerState)
        Marker(state = renoirMarkerState)
        Marker(state = cineCallaoMarkerState)
        Marker(state = doreMarkerState)
        Marker(state = kinepolisMarkerState)
        Marker(state = cineEmbajadoresMarkerState)
        Marker(state = golemMadridMarkerState)
        Marker(state = yelmoCinesIdealMarkerState)
        Marker(state = vendiMarkerState)
        Marker(state = capitolMarkerState)
    }



}
