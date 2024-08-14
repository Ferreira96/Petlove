package com.example.petlove

import com.example.petlove.repository.Pet
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petlove.formularios.FormPetActivity
import com.example.petlove.repository.deletePet
import com.example.petlove.repository.getPetImageUri
import com.google.firebase.database.FirebaseDatabase
import com.example.petlove.repository.getPets
import com.example.petlove.repository.getUsuarioPorEmail
import com.example.petlove.repository.updatePet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MeusPetsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meus_pets)

        // Inicialize o Firebase e recupere o objeto Pet
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        //AÇÃO DO BOTÃO [MEUS PETS]
        val btEntrar: Button = findViewById(R.id.bt_cadastro_pet)
        btEntrar.setOnClickListener {
            val intent = Intent(this, FormPetActivity::class.java)
            startActivity(intent)
        }

        // Recupera dados do SharedPreferences
        val sharedPreferences = getSharedPreferences("PersistenciaLogin", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("USER_EMAIL", null)


        CoroutineScope(Dispatchers.Main).launch {
            //recupera id do usuario
            var id_usuario = 0
            if (userEmail != null) {
                val usuario = getUsuarioPorEmail(userEmail)
                id_usuario = usuario?.id ?: 0 // Acessa o campo 'id' do objeto Usuario, ou 0 se for null
            }

            // Recupera a lista de pets de forma assíncrona
            var pets = getPets()
            pets = pets.filter { it.usuario_id == id_usuario }

            // Atualiza o adapter da RecyclerView com a lista de pets
            val listaPetAdocaoView = findViewById<RecyclerView>(R.id.lista_meus_pets)
            listaPetAdocaoView.adapter       = ListaMeusPets(this@MeusPetsActivity, pets)
            listaPetAdocaoView.layoutManager = LinearLayoutManager(this@MeusPetsActivity)
        }
    }
}

//*** LISTA DE VIEW ***//
class ListaMeusPets(
    private val context: Context,
    private val pets: List<Pet>
    ) : RecyclerView.Adapter<ListaMeusPets.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun vincula(pet: Pet, context: Context) {
            val nome = itemView.findViewById<TextView>(R.id.tx_pet_user_nome)
            nome.text = pet.nome

            val idade = itemView.findViewById<TextView>(R.id.tx_pet_user_idade)
            idade.text = pet.idade.toString() + "Anos"

            val peso = itemView.findViewById<TextView>(R.id.tx_pet_user_peso)
            peso.text = pet.peso.toString() + "Kg"

            //carrega imagem do banco
            CoroutineScope(Dispatchers.Main).launch {
                val img_pet_user: ImageView = itemView.findViewById(R.id.img_pet_user)
                val petImageUri = getPetImageUri(pet.id)
                if (petImageUri != null) {
                    Glide.with(itemView)
                        .load(petImageUri)
                        .into(img_pet_user)
                }
            }

            //BOTAO [EDITAR]
            val botaoEditar = itemView.findViewById<ImageView>(R.id.bt_pet_user_edt)
            botaoEditar.setOnClickListener {
                /*envia a variavel adocao para AdocaoEDoacaoActivity*/
                val intent = Intent(context, FormPetActivity::class.java)
                intent.putExtra("petToUpdate_id", pet.id)
                context.startActivity(intent)
            }

            //BOTAO [EXCLUIR]
            val botaoExcluir = itemView.findViewById<ImageView>(R.id.bt_pet_user_exc)
            botaoExcluir.setOnClickListener {
                botaoExcluir(pet)
                val intent = Intent(context, MeusPetsActivity::class.java)
                context.startActivity(intent)
            }
        }

        private fun botaoExcluir(pet: Pet) {
            CoroutineScope(Dispatchers.Main).launch {
                deletePet(pet.id)
            }
        }

        private fun botaoEditar(pet: Pet) {
            CoroutineScope(Dispatchers.Main).launch {
                updatePet(pet)
            }
        }
    }

    // CRIA O XML DOS OBJETOS
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout._pet_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pet = pets[position]
        holder.vincula(pet, context)
    }

    override fun getItemCount(): Int {
        return pets.size
    }

}
