package com.example.proyecto_final.model

data class Sesions(
    val id: String?= null,
    val sesionId: String="",
    val cine: String="",
    val sesion: String="",
    val titulo: String="",
    val latitud: Double=0.0,
    val longitud: Double=0.0,
    val img: String=""
)
