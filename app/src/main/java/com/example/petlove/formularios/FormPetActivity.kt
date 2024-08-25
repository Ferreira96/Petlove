package com.example.petlove.formularios

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.petlove.MenuActivity
import com.example.petlove.R
import com.example.petlove.repository.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FormPetActivity : AppCompatActivity() {

    private var imageUri: Uri? = null

    // Substitui onActivityResult: registra o callback para o resultado da seleção de imagem
    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            imageUri = result.data?.data
            findViewById<ImageView>(R.id.img_form_pet).setImageURI(imageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout._form_pet)

        // CARREGA DADOS SE FOR UPDATE
        val petToUpdate_id = intent.getIntExtra("petToUpdate_id", 0)
        if (petToUpdate_id != 0) {
            CoroutineScope(Dispatchers.Main).launch {
                // RECUPERA E CARREGA DADOS
                val pet = getPet(petToUpdate_id)
                pet?.let {
                    findViewById<TextView>(R.id.lb_form_pet_nome).text = it.nome
                    findViewById<TextView>(R.id.lb_form_pet_idade).text = it.idade.toString()
                    findViewById<TextView>(R.id.lb_form_pet_peso).text = it.peso.toString()
                    findViewById<CheckBox>(R.id.cb_form_pet_adocao).isChecked = it.adocao
                    findViewById<CheckBox>(R.id.cb_form_pet_doacao).isChecked = it.doacao

                    // RECUPERA E CARREGA IMG
                    val petImageUri = getPetImageUri(it.id)
                    if (petImageUri != null) {
                        Glide.with(this@FormPetActivity)
                            .load(petImageUri)
                            .into(findViewById(R.id.img_form_pet))
                    }
                }
            }
        }

        // AÇÃO DO BOTÃO [SELECIONAR FOTO(PET)]
        findViewById<Button>(R.id.bt_form_pet_selecionar).setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            selectImageLauncher.launch(intent)
        }

        // AÇÃO DO BOTÃO [SALVAR]
        findViewById<Button>(R.id.bt_form_pet_salvar).setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {

                // SESSÃO
                val sessao = getSharedPreferences("sessao", Context.MODE_PRIVATE)
                val userEmail = sessao.getString("USER_EMAIL", null)

                // RECUPERA USUARIO
                userEmail?.let { email ->
                    val usuario = getUsuarioPorEmail(email)

                    // MONTA OBJ PET
                    val pet = Pet(
                        id = petToUpdate_id,
                        usuario_id = usuario?.id ?: 0,
                        nome = findViewById<TextView>(R.id.lb_form_pet_nome).text.toString(),
                        idade = findViewById<TextView>(R.id.lb_form_pet_idade).text.toString().toIntOrNull() ?: 0,
                        peso = findViewById<TextView>(R.id.lb_form_pet_peso).text.toString().toFloatOrNull() ?: 0f,
                        adocao = findViewById<CheckBox>(R.id.cb_form_pet_adocao).isChecked,
                        doacao = findViewById<CheckBox>(R.id.cb_form_pet_doacao).isChecked
                    )

                    // PET ADD/UP
                    if (pet.id == 0) {
                        insertPet(pet, imageUri)
                    } else {
                        updatePet(pet)
                    }
                    Toast.makeText(baseContext, "Pet adicionado com sucesso", Toast.LENGTH_SHORT).show()
                }
            }

            // INTENT
            startActivity(Intent(this, MenuActivity::class.java))
        }
    }
}
