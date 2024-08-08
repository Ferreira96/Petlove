package com.example.petlove

import Pet
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import insertPet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

            var lb_nome  = findViewById<TextView>(R.id.lb_nome)
            var lb_idade  = findViewById<TextView>(R.id.lb_idade)
            var lb_peso  = findViewById<TextView>(R.id.lb_peso)
            val cbAdocao = findViewById<CheckBox>(R.id.cb_adocao)
            val cbDoacao = findViewById<CheckBox>(R.id.cb_doacao)

            CoroutineScope(Dispatchers.Main).launch {
                val pet = Pet(  id         = 0,
                                usuario_id = 1,
                                nome       = lb_nome.text.toString(),
                                idade      = lb_idade.text.toString().toIntOrNull() ?: 0,
                                peso       = lb_peso.text.toString().toIntOrNull() ?: 0,
                                adocao     = cbAdocao.isChecked,
                                doacao     = cbDoacao.isChecked,
                               )
                val result = insertPet(pet)
                if (result) {
                    Log.d("PetData", "Pet inserido com sucesso!")
                } else {
                    Log.d("PetData", "Falha ao inserir pet.")
                }
            }

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Pet adicionado com sucesso", Toast.LENGTH_SHORT).show()
        }
    }
}