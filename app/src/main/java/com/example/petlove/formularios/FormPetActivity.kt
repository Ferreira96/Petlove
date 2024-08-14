package com.example.petlove.formularios

import com.example.petlove.repository.Pet
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.petlove.HomeActivity
import com.example.petlove.R
import com.example.petlove.repository.getPet
import com.example.petlove.repository.getPetImageUri
import com.example.petlove.repository.getUsuarioPorEmail
import com.example.petlove.repository.insertPet
import com.example.petlove.repository.updatePet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FormPetActivity : AppCompatActivity() {

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_pet)

        //recupera variaveis da MeusPetsActivity
        val petToUpdate_id = intent.getIntExtra("petToUpdate_id", 0)

        if(petToUpdate_id != 0){
            CoroutineScope(Dispatchers.Main).launch {
                var pet = getPet(petToUpdate_id)
                if (pet != null) {
                    //carrega imagem do banco
                    val img_cad_pet: ImageView = findViewById(R.id.img_cad_pet)
                    val petImageUri = getPetImageUri(pet.id)
                    if (petImageUri != null) {
                        Glide.with(this@FormPetActivity)
                            .load(petImageUri)
                            .into(img_cad_pet)
                    }
                    findViewById<TextView>(R.id.lb_nome).text = pet.nome
                    findViewById<TextView>(R.id.lb_idade).text = pet.idade.toString()
                    findViewById<TextView>(R.id.lb_peso).text = pet.peso.toString()
                    findViewById<CheckBox>(R.id.cb_adocao).isChecked = pet.adocao
                    findViewById<CheckBox>(R.id.cb_doacao).isChecked = pet.doacao
                }
            }
        }

        // //AÇÃO DO BOTÃO [CARREGAR FOTO PET]
        val btAddImgPet: Button = findViewById(R.id.bt_add_img_pet)
        btAddImgPet.setOnClickListener {
            openGallery()
        }

        //AÇÃO DO BOTÃO [SALVAR]
        val btCadPet: Button = findViewById(R.id.bt_salvar)
        btCadPet.setOnClickListener {

            var lb_nome  = findViewById<TextView>(R.id.lb_nome)
            var lb_idade  = findViewById<TextView>(R.id.lb_idade)
            var lb_peso  = findViewById<TextView>(R.id.lb_peso)
            val cbAdocao = findViewById<CheckBox>(R.id.cb_adocao)
            val cbDoacao = findViewById<CheckBox>(R.id.cb_doacao)

            // Recupera dados do SharedPreferences
            val sharedPreferences = getSharedPreferences("PersistenciaLogin", Context.MODE_PRIVATE)
            val userEmail = sharedPreferences.getString("USER_EMAIL", null)

            CoroutineScope(Dispatchers.Main).launch {
                var id_usuario = 0
                if (userEmail != null) {
                    val usuario = getUsuarioPorEmail(userEmail)
                    id_usuario = usuario?.id ?: 0 // Acessa o campo 'id' do objeto Usuario, ou 0 se for null
                }

                val pet = Pet(
                    id = petToUpdate_id ?: 0,
                    usuario_id = id_usuario,
                    nome = lb_nome.text.toString(),
                    idade = lb_idade.text.toString().toIntOrNull() ?: 0,
                    peso = lb_peso.text.toString().toIntOrNull() ?: 0,
                    adocao = cbAdocao.isChecked,
                    doacao = cbDoacao.isChecked
                )

                if(pet.id ==0){
                    insertPet(pet, imageUri)
                }else{
                    updatePet(pet)
                }

            }

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Pet adicionado com sucesso", Toast.LENGTH_SHORT).show()
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
            val img_cad_pet: ImageView = findViewById(R.id.img_cad_pet)
            img_cad_pet.setImageURI(imageUri)
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}