package com.example.petlove.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

//OBJ
data class Pet(
    var id        : Int = 0,
    val usuario_id: Int = 0,
    val nome      : String = "",
    val idade     : Int = 0,
    val peso      : Float = 0f,
    val adocao    : Boolean = false,
    val doacao    : Boolean = false
)


//OPERA OS OBJETOS NO FIRESTORE
suspend fun getPet(id: Int): Pet? {
    val db = FirebaseFirestore.getInstance()
    val petRef = db.collection("pets").document(id.toString())

    return try {
        val document = petRef.get().await()
        if (document.exists()) {
            document.toObject(Pet::class.java).also {
            }
        } else {
            null
        }
    } catch (exception: Exception) {
        null
    }
}

suspend fun getPets(): List<Pet> {
    val db = FirebaseFirestore.getInstance()
    return try {
        val querySnapshot = db.collection("pets").get().await()
        querySnapshot.documents.mapNotNull { document ->
            document.toObject(Pet::class.java)
        }
    } catch (exception: Exception) {
        emptyList()
    }
}

suspend fun insertPet(pet: Pet): Boolean {
    val db = FirebaseFirestore.getInstance()
    return try {
        val maiorId = _maiorIdPet() ?: 0
        val novoId = maiorId + 1
        pet.id = novoId

        db.collection("pets").document(novoId.toString()).set(pet).await()
        true
    } catch (exception: Exception) {
        false
    }
}

suspend fun updatePet(pet: Pet): Boolean {
    val db = FirebaseFirestore.getInstance()

    return try {
        db.collection("pets").document(pet.id.toString()).set(pet).await()
        true
    } catch (exception: Exception) {
        false
    }
}

suspend fun deletePet(id: Int): Boolean {
    val db = FirebaseFirestore.getInstance()

    return try {
        db.collection("pets").document(id.toString()).delete().await()
        Log.d("PetData", "Pet with ID $id deleted successfully.")
        true
    } catch (exception: Exception) {
        Log.w("PetData", "Error deleting pet with ID $id.", exception)
        false
    }
}


//OPERA IMAGENS NO STORAGE
suspend fun getPetImg(id: Int): Uri? {
    val storageRef = FirebaseStorage.getInstance().reference
    val petImageRef = storageRef.child("pets/$id.jpg")

    return try {
        val downloadUrl = petImageRef.downloadUrl.await()
        downloadUrl
    } catch (exception: Exception) {
        null
    }
}

suspend fun insertPetImg(imageUri: Uri?): String? {
    if (imageUri == null) return null

    val maiorId = _maiorIdPet() ?: 0
    /*val novoId  = maiorId + 1*/

    val storageRef = FirebaseStorage.getInstance().reference
    val petImageRef = storageRef.child("pets/$maiorId.jpg")

    return try {
        petImageRef.putFile(imageUri).await()
        val downloadUrl = petImageRef.downloadUrl.await()
        downloadUrl.toString()
    } catch (exception: Exception) {
        null
    }
}


//GERAL
suspend fun _maiorIdPet(): Int? {
    val pets = getPets()
    return if (pets.isNotEmpty()) {
        pets.maxByOrNull { it.id }?.id
    } else {
        null
    }
}
