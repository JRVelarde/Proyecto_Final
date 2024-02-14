package com.example.proyecto_final.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.proyecto_final.model.Sesions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeScreenViewModel: ViewModel() {

    private val _sesiones = MutableStateFlow<List<Sesions>>(emptyList())
    val sesiones: StateFlow<List<Sesions>> = _sesiones
    private val db = Firebase.firestore
    fun leerDatos() {
        val sesionesList = mutableListOf<Sesions>()

       db.collection("sesiones")
           .get()
           .addOnSuccessListener { result ->
               for(document in result){
                   Log.d("Sesiones", "${document.id} => ${document.data}")
                   val sesion = document.toObject(Sesions::class.java)
                   sesionesList.add(sesion)
               }
               _sesiones.value = sesionesList
           }
           .addOnFailureListener{exception ->
               Log.d("Sesiones", "ERROR getting documents: ", exception)
           }
    }
}