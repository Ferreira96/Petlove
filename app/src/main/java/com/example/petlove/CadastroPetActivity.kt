package com.example.petlove

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class CadastroPetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //DEFINE XML
        setContentView(R.layout.activity_cadastro_pet)

        //AÇÃO DO BOTÃO [SALVAR]
        /*metodo salvar TODO BOTÃO SALVAR*/

        /*retorno para home*/
        val btCadPet: Button = findViewById(R.id.bt_salvar)
        btCadPet.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Pet adicionado com sucesso", Toast.LENGTH_SHORT).show()
        }
    }
}