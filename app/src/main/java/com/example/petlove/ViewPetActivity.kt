package com.example.petlove

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.petlove.repository.getPet
import com.example.petlove.repository.getPetImg
import com.example.petlove.repository.getUsuarioImg
import com.example.petlove.repository.getUsuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewPetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pet)

        // Carrega os dados do pet de forma assíncrona
        CoroutineScope(Dispatchers.Main).launch {

            // Recupera variáveis da MenuActivity
            val id_pet = intent.getIntExtra("pet_id", 0)
            val pet = getPet(id_pet)
            val usuario = pet?.let { getUsuario(it.usuario_id) }

            // Atualiza a UI com os dados do pet e do usuário
            if (pet != null) {
                //carrega imagem do banco
                val imageView_dog: ImageView = findViewById(R.id.img_view_pet_pet)
                val petImageUri = getPetImg(pet.id)
                if (petImageUri != null) {
                    Glide.with(this@ViewPetActivity)
                        .load(petImageUri)
                        .into(imageView_dog)
                }
                findViewById<TextView>(R.id.tx_view_pet_nome_pet).text = pet.nome
                findViewById<TextView>(R.id._view_pet_pet_idade).text = "Idade: " + pet.idade.toString() + " Anos"
                findViewById<TextView>(R.id.tx_view_pet_pet_peso).text  = "Peso: " +pet.peso.toString() + " Kg"
            }

            if (usuario != null) {
                //carrega imagem do banco
                val imageView_dono: ImageView = findViewById(R.id.img_view_pet_usuario)
                val petImageUri = getUsuarioImg(usuario.id)
                if (petImageUri != null) {
                    Glide.with(this@ViewPetActivity)
                        .load(petImageUri)
                        .into(imageView_dono)
                }

                findViewById<TextView>(R.id.tx_view_pet_usuario_nome).text = usuario.nome
                findViewById<TextView>(R.id.tx_view_pet_usuario_contato).text = "Contato: " + usuario.celular
            }
        }
    }
}
