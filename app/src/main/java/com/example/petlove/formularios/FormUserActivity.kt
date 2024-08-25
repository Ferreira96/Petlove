package com.example.petlove.formularios

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petlove.R
import com.example.petlove.repository.Usuario
import com.example.petlove.repository.addUsuario
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FormUserActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout._form_user)

        auth = Firebase.auth/**AUTENTICAÇÃO*/
        var lb_cad_nome  = findViewById<EditText>(R.id.lb_form_user_nome)
        var lb_cad_celular  = findViewById<EditText>(R.id.lb_form_user_celular)
        var lb_cad_email  = findViewById<EditText>(R.id.lb_form_user_email)
        var lb_cad_senha  = findViewById<EditText>(R.id.lb_form_user_senha)


        // //AÇÃO DO BOTÃO [CARREGAR FOTO USER]
        val btAddImgPet: Button = findViewById(R.id.bt_form_user_selecionar)
        btAddImgPet.setOnClickListener {
            openGallery()
            val img_cad_user: ImageView = findViewById(R.id.img_form_user)
            img_cad_user.setImageURI(imageUri)
        }

        //AÇÃO DO BOTÃO [CADASTRO]
        val btCadastro: Button = findViewById(R.id.txbt_form_user_cadastrar)
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
                    addUsuario(novoUsuario, imageUri)
                }
                Toast.makeText(baseContext, "Usuario criado com sucesso", Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(baseContext, "Falha na autenticação", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data

        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}


