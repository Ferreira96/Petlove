package com.example.petlove

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
import com.example.petlove.repository.Pet
import com.example.petlove.repository.getPetImg
import com.example.petlove.repository.getPets
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdocaoEDoacaoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pets_operations_list)

        //BOTAO [HOME]
        val botaoHome = findViewById<ImageView>(R.id.img_pet_my)
        botaoHome.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }

        //RECUPERA VARIÁVEL DA INTENT
        val adocao = intent.getBooleanExtra("adocao", false)
        val doacao = intent.getBooleanExtra("doacao", false)
        var busca  = intent.getStringExtra("busca")

        //BOTÃO [BUSCA]
        findViewById<Button>(R.id.bt_pets_operacoes_busca).setOnClickListener {
            val intent = Intent(this, AdocaoEDoacaoActivity::class.java)
            val lb_pets_operacoes_busca = findViewById<TextView>(R.id.lb_pets_operacoes_busca).text.toString()

            if(adocao){intent.putExtra("adocao", true)/*envia var*/}
            if(adocao){intent.putExtra("doacao", true)/*envia var*/}
            intent.putExtra("busca", lb_pets_operacoes_busca)/*envia var*/
            startActivity(intent)
        }

        if (adocao) {
            findViewById<TextView>(R.id.tx_top).text = "Adoção"
        } else if (doacao) {
            findViewById<TextView>(R.id.tx_top).text = "Doação"
        }

        //CONFIGURA LISTA
        CoroutineScope(Dispatchers.Main).launch {

            //FILTROS
            var pets = getPets()

            if(adocao){
                pets = pets.filter { it.adocao }
            }else if (doacao) {
                pets = pets.filter { it.doacao }
            }

            if (!busca.isNullOrEmpty()) {
                pets = pets.filter { pet ->
                    pet.nome.contains(busca, ignoreCase = true)
                }
            }

            /*CONFIGURA LISTA  <_pet_operations>*/
            val listaPetAdocaoView = findViewById<RecyclerView>(R.id.list_pets_operacoes)
            listaPetAdocaoView.adapter = ListaPets(this@AdocaoEDoacaoActivity, pets)
            listaPetAdocaoView.layoutManager = LinearLayoutManager(this@AdocaoEDoacaoActivity)
        }
    }
}

//CONFIGURA LISTA  <_pet_operations>
class ListaPets(
    private val context: Context,
    private val pets: List<Pet>
) : RecyclerView.Adapter<ListaPets.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun vincula(pet: Pet, context: Context) {

            //CARREGA DADOS DO FIRESTORE
            val nome = itemView.findViewById<TextView>(R.id.tx_pet_my_nome)
            nome.text = pet.nome

            val idade = itemView.findViewById<TextView>(R.id.tx_pet_my_idade)
            idade.text = pet.idade.toString() + "Anos"

            val peso = itemView.findViewById<TextView>(R.id.tx_pet_my_peso)
            peso.text = pet.peso.toString() + "Kg"

            //CARREGA IMAGEM DO STORAGE
            CoroutineScope(Dispatchers.Main).launch {
                val imageView: ImageView = itemView.findViewById(R.id.img_pet_my)
                val petImageUri = getPetImg(pet.id)

                if (petImageUri != null) {
                    Glide.with(itemView)
                        .load(petImageUri)
                        .into(imageView)
                }
            }

            //BOTÃO [VER PERFIL]
            val btPetPerfil = itemView.findViewById<Button>(R.id.bt_pet_my)
            btPetPerfil.setOnClickListener {
                val intent = Intent(context, ViewPetActivity::class.java)
                intent.putExtra("pet_id", pet.id)
                context.startActivity(intent)
            }
        }
    }

    //OPERAÇOES BASICAS RecyclerView
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
