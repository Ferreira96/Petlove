package com.example.petlove.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

data class Usuario(
    var id: Int = 0,
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

suspend fun getUsuarioPorEmail(email: String): Usuario? {
    val db = FirebaseFirestore.getInstance()
    val usuariosRef = db.collection("usuarios")

    return try {
        // Faz uma consulta para encontrar o usuário com o e-mail fornecido
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

suspend fun addUsuario(usuario: Usuario, imageUri: Uri?): Boolean {
    val db = FirebaseFirestore.getInstance()

    return try {
        // Obtém o maior ID atual e define o próximo ID
        val maiorId = getMaiorIdUsuario() ?: 0
        val novoId = maiorId + 1

        usuario.id = novoId

        // Adiciona o novo usuário ao Firestore
        db.collection("usuarios").document(novoId.toString()).set(usuario).await()
        // Adiciona imagem ao Storage
        if (imageUri != null){
            saveUserImageToStorage(imageUri, novoId)
        }
        Log.d("UsuarioData", "Usuário adicionado com sucesso: $usuario")
        true // Retorna true se o usuário foi adicionado com sucesso
    } catch (exception: Exception) {
        Log.w("UsuarioData", "Erro ao adicionar usuário.", exception)
        false // Retorna false se houve falha na adição do usuário
    }
}

suspend fun getMaiorIdUsuario(): Int? {
    val usuarios = getUsuarios()

    return if (usuarios.isNotEmpty()) {
        usuarios.maxByOrNull { it.id }?.id
    } else {
        null
    }
}

suspend fun saveUserImageToStorage(imageUri: Uri?, id: Int): String? {
    if (imageUri == null) return null

    val storageRef = FirebaseStorage.getInstance().reference
    val userImageRef = storageRef.child("usuarios/$id.jpg")

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

//carrega imagem do banco
suspend fun getUserImageUri(id: Int): Uri? {
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