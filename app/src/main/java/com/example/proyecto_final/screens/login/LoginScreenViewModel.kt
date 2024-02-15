package com.example.proyecto_final.screens.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_final.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch


class LoginScreenViewModel: ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit) = viewModelScope.launch {
            try{
               auth.signInWithEmailAndPassword(email, password)
                   .addOnCompleteListener{task ->
                       if(task.isSuccessful){
                           Log.d("Logueado","signInWithEmailAndPassword loguado!!")
                           home()
                       }
                       else{
                           Log.d("Logueado","signInWithEmailAndPassword: ${task.result}")

                       }

                   }
            }catch (ex:Exception){
                    Log.d("Logueado", "signInWithEmailAndPassword: ${ex.message}")
            }
    }
    fun createUserWithEmailAndPassword(
        email:String,
        password: String,
        home: () -> Unit
    ){
        if(_loading.value == false){
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        val displayName =
                            task.result.user?.email?.split("@")?.get(0)
                        createUser(displayName)
                        home()
                    }
                    else{
                        Log.d("Logueado", "createUserWithEmailAndPassword: ${task.result}")
                    }
                    _loading.value = false
                }
        }
    }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid
        val email = auth.currentUser?.email
        //val user = mutableMapOf<String, Any>()

        //user["user_id"] = userId.toString()
        //user["display_name"] = displayName.toString()
        val user = User(
            userId = userId.toString(),
            email = email.toString(),
            displayName = displayName.toString(),
            id = null
        ).toMap()
        FirebaseFirestore.getInstance().collection("usuarios")
            .add(user)
            .addOnSuccessListener {
                Log.d("Logueado", "Creado ${it.id}")
            }.addOnFailureListener{
                Log.d("Logueado", "Ocurrió error $it")
            }
    }
}

