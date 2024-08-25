package com.example.petlove.formularios

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.petlove.R
import com.example.petlove.repository.Usuario
import com.example.petlove.repository.insertUsuario
import com.example.petlove.repository.insertUsuarioImg
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FormUserActivity : AppCompatActivity() {

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout._form_user)

        //TODO UPDATE USER

        //BOTÃO [SELECIONAR FOTO(USER)]
        val selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                imageUri = result.data?.data
                findViewById<ImageView>(R.id.img_form_user).setImageURI(imageUri)
            }
        }
        findViewById<Button>(R.id.bt_form_user_selecionar).setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
            selectImageLauncher.launch(intent)
        }

        //BOTÃO [SALVAR]
        findViewById<Button>(R.id.txbt_form_user_salvar).setOnClickListener {
            val cad_email = findViewById<EditText>(R.id.lb_form_user_email).text.toString()
            val cad_senha = findViewById<EditText>(R.id.lb_form_user_senha).text.toString()

            val novoUsuario = Usuario(
                id = 0,
                nome = findViewById<EditText>(R.id.lb_form_user_nome).text.toString(),
                email = findViewById<EditText>(R.id.lb_form_user_email).text.toString(),
                celular = findViewById<EditText>(R.id.lb_form_user_celular).text.toString()
            )

            //AUTH + FIRESTORE (REGISTER)
            val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }/*auth*/
            auth.createUserWithEmailAndPassword(cad_email, cad_senha).addOnCompleteListener{ task->
                if(task.isSuccessful){
                    CoroutineScope(Dispatchers.Main).launch {
                        insertUsuario(novoUsuario)
                        insertUsuarioImg(imageUri)
                        Toast.makeText(baseContext, "Usuario cadastrado com sucesso", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}


