package com.example.petlove.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

//OBJ
data class Usuario(
    var id: Int = 0,
    val nome: String = "",
    val email: String = "",
    val celular: String = ""
)


//OPERA OS OBJETOS NO FIRESTORE
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

suspend fun getUsuarioPorEmail(email: String): Usuario? {
    val db = FirebaseFirestore.getInstance()
    val usuariosRef = db.collection("usuarios")

    return try {
        val querySnapshot = usuariosRef.whereEqualTo("email", email).get().await()

        if (!querySnapshot.isEmpty) {
            val document = querySnapshot.documents[0] // Assume que o e-mail é único
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

suspend fun insertUsuario(usuario: Usuario): Boolean {
    val db = FirebaseFirestore.getInstance()

    return try {
        val maiorId = _maiorIdUsuario() ?: 0
        val novoId = maiorId + 1
        usuario.id = novoId

        db.collection("usuarios").document(novoId.toString()).set(usuario).await()
        true
    } catch (exception: Exception) {
        false
    }
}


//OPERA IMAGENS NO STORAGE
suspend fun getUsuarioImg(id: Int): Uri? {
    val storageRef = FirebaseStorage.getInstance().reference
    val userImageRef = storageRef.child("usuarios/$id.jpg")

    return try {
        val downloadUrl = userImageRef.downloadUrl.await()
        Log.d("UserImage", "Image URI fetched successfully: $downloadUrl")
        downloadUrl
    } catch (exception: Exception) {
        Log.w("UserImage", "Error fetching image URI.", exception)
        null
    }
}

suspend fun insertUsuarioImg(imageUri: Uri?): String? {
    if (imageUri == null) return null

    val maiorId = _maiorIdUsuario() ?: 0
    /*val novoId = maiorId + 1*/

    val storageRef = FirebaseStorage.getInstance().reference
    val userImageRef = storageRef.child("usuarios/$maiorId.jpg")

    return try {
        userImageRef.putFile(imageUri).await()
        val downloadUrl = userImageRef.downloadUrl.await()
        Log.d("UserImage", "Image uploaded successfully: $downloadUrl")
        downloadUrl.toString()
    } catch (exception: Exception) {
        Log.w("UserImage", "Error uploading image.", exception)
        null
    }
}


//GERAL
suspend fun _maiorIdUsuario(): Int? {
    val usuarios = getUsuarios()

    return if (usuarios.isNotEmpty()) {
        usuarios.maxByOrNull { it.id }?.id
    } else {
        null
    }
}