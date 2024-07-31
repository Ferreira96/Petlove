package com.example.petlove

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //DEFINE XML
        setContentView(R.layout.activity_login)

        //MANIPULA TX_TITULO
        var tx_titulo  = findViewById<TextView>(R.id.tx_titulo)
        tx_titulo.setText("-  PETLOVE  -")

        //AÇÃO DO BOTÃO [LOGIN]
        val btEntrar: Button = findViewById(R.id.bt_entrar)
        btEntrar.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}