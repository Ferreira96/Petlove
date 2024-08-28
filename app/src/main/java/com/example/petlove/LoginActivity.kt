package com.example.petlove

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petlove.formularios.FormUserActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //BOTÃO [ENTRAR].
        findViewById<Button>(R.id.bt_login_entrar).setOnClickListener {
            var lb_login_usuario = findViewById<EditText>(R.id.lb_login_usuario).text.toString()
            var lb_login_senha   = findViewById<EditText>(R.id.lb_login_senha).text.toString()

            if(!lb_login_usuario.equals("") or !lb_login_senha.equals("")){
                login(lb_login_usuario, lb_login_senha)
            }
        }

        //BOTÃO [CADASTRAR]
        findViewById<TextView>(R.id.txbt_login_cadastrar).setOnClickListener {
            startActivity(Intent(this, FormUserActivity::class.java))
        }
    }

    //FIREBASE AUTH (LOGIN)
    private fun login(email: String, password: String){
        val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{ task->
            if(task.isSuccessful){
                Toast.makeText(baseContext, "Usuario logado com sucesso", Toast.LENGTH_SHORT).show()

                //SESSÃO
                val sessao = getSharedPreferences("sessao", Context.MODE_PRIVATE)
                sessao.edit().putString( "USER_EMAIL", email).apply()
                sessao.edit().putBoolean("IS_LOGGED" , true).apply()

                //INTENT
                startActivity(Intent(this, MenuActivity::class.java))
            }else{
                Toast.makeText(baseContext, "Senha ou usuario incorretos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}