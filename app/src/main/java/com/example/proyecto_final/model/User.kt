package com.example.proyecto_final.model

data class User(
    val id: String?,
    val userId: String,
    val email: String,
    val displayName: String
){
    fun toMap(): MutableMap<String, Any>{
        return mutableMapOf(
            "user_id" to this.userId,
            "email" to this.email,
            "display_name" to this.displayName
        )
    }
}
