package com.example.petlove

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        //AÇÃO DO BOTÃO [MEUS PETS].
        findViewById<Button>(R.id.bt_menu_meus).setOnClickListener {
            startActivity(Intent(this, MeusPetsActivity::class.java))
        }

        //AÇÃO DO BOTÃO [ADOÇÃO]
        findViewById<Button>(R.id.bt_menu_adocoes).setOnClickListener {
            val intent = Intent(this, AdocaoEDoacaoActivity::class.java)
            intent.putExtra("adocao", true)/*envia var*/
            startActivity(intent)
        }

        //AÇÃO DO BOTÃO [DOAÇÃO]
        findViewById<Button>(R.id.bt_menu_doacoes).setOnClickListener {
            val intent = Intent(this, AdocaoEDoacaoActivity::class.java)
            intent.putExtra("doacao", true)/*envia var*/
            startActivity(intent)
        }
    }
}