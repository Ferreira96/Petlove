package com.example.petlove.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
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

suspend fun insertPet(pet: Pet): Boolean {
    val db = FirebaseFirestore.getInstance()

    return try {
        val maiorId = getMaiorIdPet() ?: 0
        val novoId = maiorId + 1

        pet.id = novoId

        // Adiciona o novo usuário ao Firestore
        db.collection("pets").document(novoId.toString()).set(pet).await()

        // Adiciona ou atualiza o documento
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