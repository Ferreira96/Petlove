package com.example.petlove

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PerfilPetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*recupera variaveis da HomeActivity*/
        val id_pet  = intent.getIntExtra("pet_id", 0)
        val pet     = GetPetsTESTE().getPet(id_pet)
        val usuario = pet?.let { GetUsuariosTESTE().getUsuario(it.usuario_id) }

        //DEFINE XML
        setContentView(R.layout.activity_perfil_pet)

        if (pet != null) {
            findViewById<TextView>(R.id.tx_nome_dog).setText(pet.nome)
            findViewById<TextView>(R.id.tx_idade_dog).setText(pet.idade)
            findViewById<TextView>(R.id.tx_peso_dog).setText(pet.peso)
        }

        if (usuario != null) {
            findViewById<TextView>(R.id.tx_nome_dono).setText(usuario.nome)
            findViewById<TextView>(R.id.tx_contato_dono).setText(usuario.celular)
        }
    }
}
