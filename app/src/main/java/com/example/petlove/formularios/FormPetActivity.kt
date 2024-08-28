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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout._form_pet)


        //CARREGA DADOS SE FOR UPDATE.
        val petToUpdate_id = intent.getIntExtra("petToUpdate_id", 0)
        if (petToUpdate_id != 0) {
            CoroutineScope(Dispatchers.Main).launch {
                //RECUPERA E CARREGA DADOS
                val pet = getPet(petToUpdate_id)
                pet?.let {
                    findViewById<TextView>(R.id.lb_form_pet_nome).text = it.nome
                    findViewById<TextView>(R.id.lb_form_pet_especie).text = it.especie
                    findViewById<TextView>(R.id.lb_form_pet_idade).text = it.idade.toString()
                    findViewById<TextView>(R.id.lb_form_pet_peso).text = it.peso.toString()
                    findViewById<CheckBox>(R.id.cb_form_pet_adocao).isChecked = it.adocao
                    findViewById<CheckBox>(R.id.cb_form_pet_doacao).isChecked = it.doacao

                    //RECUPERA E CARREGA IMG
                    val petImageUri = getPetImg(it.id)
                    if (petImageUri != null) {
                        imageUri = petImageUri /*Atribui a URI da imagem existente a imageUri*/
                        Glide.with(this@FormPetActivity)
                            .load(petImageUri)
                            .into(findViewById(R.id.img_form_pet))
                    }
                }
            }
        }

        //BOTÃO [SELECIONAR FOTO(PET)]
        val selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                imageUri = result.data?.data
                findViewById<ImageView>(R.id.img_form_pet).setImageURI(imageUri)
            }
        }
        findViewById<Button>(R.id.bt_form_pet_selecionar).setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
            selectImageLauncher.launch(intent)
        }

        //BOTÃO [SALVAR]
        findViewById<Button>(R.id.bt_form_pet_salvar).setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {

                //SESSÃO
                val sessao = getSharedPreferences("sessao", Context.MODE_PRIVATE)
                val userEmail = sessao.getString("USER_EMAIL", null)

                //VALIDA USUARIO
                userEmail?.let { email ->

                    //MONTA OBJ PET
                    val pet = Pet(
                        id = petToUpdate_id,
                        usuario_id = getUsuarioPorEmail(email)?.id ?: 0,
                        nome = findViewById<TextView>(R.id.lb_form_pet_nome).text.toString(),
                        especie = findViewById<TextView>(R.id.lb_form_pet_especie).text.toString(),
                        idade = findViewById<TextView>(R.id.lb_form_pet_idade).text.toString().toIntOrNull() ?: 0,
                        peso = findViewById<TextView>(R.id.lb_form_pet_peso).text.toString().toFloatOrNull() ?: 0f,
                        adocao = findViewById<CheckBox>(R.id.cb_form_pet_adocao).isChecked,
                        doacao = findViewById<CheckBox>(R.id.cb_form_pet_doacao).isChecked
                    )

                    /*
                    //VALIDA CAMPOS
                    if (imageUri == null) {
                        Toast.makeText(baseContext, "Selecione uma imagem para o pet", Toast.LENGTH_SHORT).show()
                        return@launch
                    }
                    */


                    //PET INSERT
                    if (pet.id == 0) {
                        insertPet(pet)/*insert firebase*/
                        insertPetImg(imageUri)
                    } else {
                        updatePet(pet)/*update firebase*/
                        updateImgPet(pet.id, imageUri)
                    }

                    Toast.makeText(baseContext, "Pet adicionado com sucesso", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@FormPetActivity, MenuActivity::class.java))
                }
            }
        }
    }
}
