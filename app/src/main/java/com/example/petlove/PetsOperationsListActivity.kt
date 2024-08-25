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
import com.example.petlove.repository.getPetImageUri
import com.google.firebase.database.FirebaseDatabase
import com.example.petlove.repository.getPets
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdocaoEDoacaoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //DEFINE XML
        setContentView(R.layout.activity_pets_operations_list)

        //recupera variaveis da MenuActivity
        val adocao = intent.getBooleanExtra("adocao", false)
        val doacao = intent.getBooleanExtra("doacao", false)

        if (adocao) {
            findViewById<TextView>(R.id.tx_top).text = "Adoção"
        } else if (doacao) {
            findViewById<TextView>(R.id.tx_top).text = "Doação"
        }

        // Inicialize o Firebase e recupere o objeto Pet
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        // Recupera a lista de pets de forma assíncrona
        CoroutineScope(Dispatchers.Main).launch {
            var pets = getPets()

            if(adocao){
                pets = pets.filter { it.adocao }
            }else if (doacao) {
                pets = pets.filter { it.doacao }
            }

            // Atualiza o adapter da RecyclerView com a lista de pets
            val listaPetAdocaoView = findViewById<RecyclerView>(R.id.list_pets_operacoes)
            listaPetAdocaoView.adapter = ListaPets(this@AdocaoEDoacaoActivity, pets)
            listaPetAdocaoView.layoutManager = LinearLayoutManager(this@AdocaoEDoacaoActivity)
        }
    }
}

//*** LISTA DE VIEW ***//
class ListaPets(
    private val context: Context,
    private val pets: List<Pet>
) : RecyclerView.Adapter<ListaPets.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun vincula(pet: Pet, context: Context) {
            val nome = itemView.findViewById<TextView>(R.id.tx_pet_my_nome)
            nome.text = pet.nome

            val idade = itemView.findViewById<TextView>(R.id.tx_pet_my_idade)
            idade.text = pet.idade.toString() + "Anos"

            val peso = itemView.findViewById<TextView>(R.id.tx_pet_my_peso)
            peso.text = pet.peso.toString() + "Kg"

            //carrega imagem do banco
            CoroutineScope(Dispatchers.Main).launch {
                val imageView: ImageView = itemView.findViewById(R.id.img_pet_my)
                val petImageUri = getPetImageUri(pet.id)

                if (petImageUri != null) {
                    Glide.with(itemView)
                        .load(petImageUri)
                        .into(imageView)
                }
            }

            // AÇÃO DO BOTÃO [VER PERFIL]
            val btPetPerfil = itemView.findViewById<Button>(R.id.bt_pet_my)
            btPetPerfil.setOnClickListener {
                val intent = Intent(context, VIewPetActivity::class.java)

                /*envia a variavel adocao para VIewPetActivity*/
                intent.putExtra("pet_id", pet.id)

                context.startActivity(intent)
            }
        }
    }

    // CRIA O XML DOS OBJETOS
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout._pet_my, parent, false)
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