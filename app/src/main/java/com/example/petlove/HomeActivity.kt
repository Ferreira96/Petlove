package com.example.petlove

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //DEFINE XML
        setContentView(R.layout.activity_home)

        //AÇÃO DO BOTÃO [MEUS PETS]
        val btCadPet: Button = findViewById(R.id.bt_cadpet)
        btCadPet.setOnClickListener {
            val intent = Intent(this, MeusPetsActivity::class.java)
            startActivity(intent)
        }

        //AÇÃO DO BOTÃO [ADOÇÃO]
        val btCadastro: Button = findViewById(R.id.bt_adocao)
        btCadastro.setOnClickListener {
            val intent = Intent(this, AdocaoEDoacaoActivity::class.java)

            /*envia a variavel adocao para AdocaoEDoacaoActivity*/
            intent.putExtra("adocao", true)

            startActivity(intent)
        }

        //AÇÃO DO BOTÃO [DOAÇÃO]
        val btDoacao: Button = findViewById(R.id.bt_doacao)
        btDoacao.setOnClickListener {
            val intent = Intent(this, AdocaoEDoacaoActivity::class.java)

            /*envia a variavel doacao para AdocaoEDoacaoActivity*/
            intent.putExtra("doacao", true)

            startActivity(intent)
        }
    }
}