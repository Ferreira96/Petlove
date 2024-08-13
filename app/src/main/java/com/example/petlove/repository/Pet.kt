package com.example.petlove.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

data class Pet(
    var id: Int = 0,
    val usuario_id: Int = 0,
    val nome: String = "",
    val idade: Int = 0,
    val peso: Int = 0,
    val adocao: Boolean = false,
    val doacao: Boolean = false
)

suspend fun getPet(id: Int): Pet? {
    val db = FirebaseFirestore.getInstance()
    val petRef = db.collection("pets").document(id.toString())

    return try {
        val document = petRef.get().await()
        if (document.exists()) {
            document.toObject(Pet::class.java).also {
                Log.d("PetData", "Pet data: $it")
            }
        } else {
            Log.d("PetData", "No such document!")
            null
        }
    } catch (exception: Exception) {
        Log.w("PetData", "Error getting document.", exception)
        null
    }
}

suspend fun getPets(): List<Pet> {
    val db = FirebaseFirestore.getInstance()

    return try {
        val querySnapshot = db.collection("pets").get().await()
        querySnapshot.documents.mapNotNull { document ->
            document.toObject(Pet::class.java)
        }.also {
            Log.d("PetData", "Total pets: ${it.size}")
        }
    } catch (exception: Exception) {
        Log.w("PetData", "Error getting documents.", exception)
        emptyList()
    }
}

//PARA UTILIZAR OS METODOS
//CoroutineScope(Dispatchers.Main).launch {}

suspend fun insertPet(pet: Pet, imageUri: Uri?): Boolean {
    val db = FirebaseFirestore.getInstance()

    return try {
        val maiorId = getMaiorIdPet() ?: 0
        val novoId = maiorId + 1

        pet.id = novoId

        // Adiciona o novo pet ao Firestore
        db.collection("pets").document(novoId.toString()).set(pet).await()
        // Adiciona imagem ao Storage
        if (imageUri != null){
            savePetImageToStorage(imageUri, novoId)
        }
        Log.d("PetData", "Pet inserted with ID: ${novoId}")
        true
    } catch (exception: Exception) {
        Log.w("PetData", "Error inserting document.", exception)
        false
    }
}

suspend fun getMaiorIdPet(): Int? {
    val pets = getPets()

    return if (pets.isNotEmpty()) {
        pets.maxByOrNull { it.id }?.id
    } else {
        null
    }
}


suspend fun updatePet(pet: Pet): Boolean {
    val db = FirebaseFirestore.getInstance()

    return try {
        db.collection("pets").document(pet.id.toString()).set(pet).await()
        Log.d("PetData", "Pet with ID ${pet.id} updated successfully.")
        true
    } catch (exception: Exception) {
        Log.w("PetData", "Error updating pet with ID ${pet.id}.", exception)
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

suspend fun savePetImageToStorage(imageUri: Uri?, id: Int): String? {
    if (imageUri == null) return null

    val storageRef = FirebaseStorage.getInstance().reference
    val petImageRef = storageRef.child("pets/$id.jpg")

    return try {
        petImageRef.putFile(imageUri).await()
        val downloadUrl = petImageRef.downloadUrl.await()
        Log.d("PetImage", "Image uploaded successfully: $downloadUrl")
        downloadUrl.toString()
    } catch (exception: Exception) {
        Log.w("PetImage", "Error uploading image.", exception)
        null
    }
}