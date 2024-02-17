package com.example.proyecto_final.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.proyecto_final.model.Sesion
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class HomeScreenViewModel : ViewModel() {

    private val _sesiones = MutableStateFlow(
        SessionsUiState(
            sessionsList = emptyList(),
            currentSession = Sesion(), // Puedes ajustar esto según tus necesidades
            isShowingDetailsPage = false
        )
    )
    val sesiones: StateFlow<SessionsUiState> = _sesiones
    private val db = Firebase.firestore


    fun leerDatos() {
        val sesionesList = mutableListOf<Sesion>()
        db.collection("sesiones")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    try {
                        Log.d("Sesiones", "${document.id} => ${document.data}")
                        // Intentar deserializar directamente sin verificar campos
                        val sesion = document.toObject(Sesion::class.java)
                        sesionesList.add(sesion)
                    } catch (e: Exception) {
                        Log.e("Sesiones", "Error al procesar documento: ${document.id}", e)
                    }
                }
                _sesiones.value = SessionsUiState(
                    sessionsList = sesionesList,
                    currentSession = _sesiones.value.currentSession, // Mantén el estado actual del detalle
                    isShowingDetailsPage = _sesiones.value.isShowingDetailsPage
                )
            }
            .addOnFailureListener { exception ->
                Log.e("Sesiones", "ERROR al obtener documentos", exception)
            }
    }
    fun updateCurrentSession(selectedSession: Sesion){
        _sesiones.update {
            it.copy(currentSession = selectedSession)
        }
    }
    fun navigateToDetailsPage() {
        _sesiones.update {
            it.copy(isShowingDetailsPage = true)
        }
    }

    fun navigateBackToListPage() {
        _sesiones.update {
            it.copy(isShowingDetailsPage = false)
        }
    }
}

data class SessionsUiState(
    val sessionsList: List<Sesion> = emptyList(),
    val currentSession: Sesion = Sesion(),
    val isShowingDetailsPage: Boolean = false
)