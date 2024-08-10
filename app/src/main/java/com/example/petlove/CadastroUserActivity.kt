package com.example.petlove

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petlove.repository.Usuario
import com.example.petlove.repository.addUsuario
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CadastroUserActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        auth = Firebase.auth/**AUTENTICAÇÃO*/
        var lb_cad_nome  = findViewById<EditText>(R.id.lb_cad_nome)
        var lb_cad_celular  = findViewById<EditText>(R.id.lb_cad_celular)
        var lb_cad_email  = findViewById<EditText>(R.id.lb_cad_email)
        var lb_cad_senha  = findViewById<EditText>(R.id.lb_cad_senha)


        //AÇÃO DO BOTÃO [CADASTRO]
        val btCadastro: Button = findViewById(R.id.bt_cadastro)
        btCadastro.setOnClickListener {

            val cad_email = lb_cad_email.text.toString()
            val cad_senha = lb_cad_senha.text.toString()
            val cad_nome    = lb_cad_nome.text.toString()
            val cad_celular = lb_cad_celular.text.toString()

            if(!cad_email.equals("") or !cad_senha.equals("")){
                val novoUsuario = Usuario(
                    id = 0,
                    nome = cad_nome,
                    email = cad_email,
                    celular = cad_celular
                )

                createUser(cad_email, cad_senha, novoUsuario)
            }else{
                Toast.makeText(baseContext, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }

        }

    }

    /**AUTENTICAÇÃO*/
    private fun createUser(email: String, password: String, novoUsuario: Usuario){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{ task->
            if(task.isSuccessful){

                CoroutineScope(Dispatchers.Main).launch {
                    addUsuario(novoUsuario)
                }
                Toast.makeText(baseContext, "Usuario criado com sucesso", Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(baseContext, "Falha na autenticação", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


