package com.example.petlove

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth /**AUTENTICAÇÃO*/
    private var isLogged = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //DEFINE XML
        setContentView(R.layout.activity_login)

        auth = Firebase.auth/**AUTENTICAÇÃO*/
        var lb_usuario  = findViewById<EditText>(R.id.lb_usuario)
        var lb_senha  = findViewById<EditText>(R.id.lb_senha)

        //MANIPULA TX_TITULO
        var tx_titulo  = findViewById<TextView>(R.id.tx_titulo)
        tx_titulo.setText("-  PETLOVE  -")

        //AÇÃO DO BOTÃO [ENTRAR]
        val btEntrar: Button = findViewById(R.id.bt_entrar)
        btEntrar.setOnClickListener {
            var lb_usuario_login = lb_usuario.text.toString()
            var lb_senha_login = lb_senha.text.toString()

            if(!lb_usuario_login.equals("") or !lb_senha_login.equals("")){
                signInWithUserComEmailESenha(lb_usuario_login, lb_senha_login)
                if(isLogged){
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
            }else{
                Toast.makeText(baseContext, "email ou senha invalidos", Toast.LENGTH_SHORT).show()
            }
        }

        //AÇÃO DO TEXTVIEW [CADASTRO]
        val btCadastro: TextView = findViewById(R.id.bt_cadastro)
        btCadastro.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
            startActivity(intent)
        }
    }

    /**LOGIN*/
    private fun signInWithUserComEmailESenha(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{ task->
            if(task.isSuccessful){
                isLogged = true
                Log.d("EmailAndPassword","signInUserComEmailESenha-Sucesso")
                Toast.makeText(baseContext, "Login realizado com sucesso", Toast.LENGTH_SHORT).show()
                //val user = auth.currentUser
            }else{
                Log.d("EmailAndPassword","signInUserComEmailESenha-Falha", task.exception)
                Toast.makeText(baseContext, "email ou senha invalidos", Toast.LENGTH_SHORT).show()
                isLogged = false
            }
        }
    }
}