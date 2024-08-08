package com.example.petlove

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class Usuario(
    val id: Int = 0,
    val nome: String = "",
    val email: String = "",
    val celular: String = ""
)

suspend fun getUsuario(id: Int): Usuario? {
    val db = FirebaseFirestore.getInstance()
    val usuarioRef = db.collection("usuarios").document(id.toString())

    return try {
        val document = usuarioRef.get().await()
        if (document.exists()) {
            document.toObject(Usuario::class.java).also {
                Log.d("UsuarioData", "Usuario data: $it")
            }
        } else {
            Log.d("UsuarioData", "No such document!")
            null
        }
    } catch (exception: Exception) {
        Log.w("UsuarioData", "Error getting document.", exception)
        null
    }
}

suspend fun getUsuarios(): List<Usuario> {
    val db = FirebaseFirestore.getInstance()

    return try {
        val querySnapshot = db.collection("usuarios").get().await()
        querySnapshot.documents.mapNotNull { document ->
            document.toObject(Usuario::class.java)
        }.also {
            Log.d("UsuarioData", "Total usuarios: ${it.size}")
        }
    } catch (exception: Exception) {
        Log.w("UsuarioData", "Error getting documents.", exception)
        emptyList()
    }
}
