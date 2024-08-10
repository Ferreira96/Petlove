package com.example.petlove

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.petlove.repository.getPet
import com.example.petlove.repository.getUsuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PerfilPetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Define XML
        setContentView(R.layout.activity_perfil_pet)

        // Carrega os dados do pet de forma assíncrona
        CoroutineScope(Dispatchers.Main).launch {

            // Recupera variáveis da HomeActivity
            val id_pet = intent.getIntExtra("pet_id", 0)
            val pet = getPet(id_pet)
            val usuario = pet?.let { getUsuario(it.usuario_id) }

            // Atualiza a UI com os dados do pet e do usuário
            if (pet != null) {
                findViewById<TextView>(R.id.tx_nome_dog).text = pet.nome
                findViewById<TextView>(R.id.tx_idade_dog).text = "Idade: " + pet.idade.toString() + " Anos"
                findViewById<TextView>(R.id.tx_peso_dog).text  = "Peso: " +pet.peso.toString() + " Kg"
            }

            if (usuario != null) {
                findViewById<TextView>(R.id.tx_nome_dono).text = usuario.nome
                findViewById<TextView>(R.id.tx_contato_dono).text = "Contato: " + usuario.celular
            }
        }
    }
}
