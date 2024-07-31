package com.example.petlove

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AdocaoEDoacaoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //DEFINE XML
        setContentView(R.layout.activity_adocao_e_doacao)

        /*recupera variaveis da HomeActivity*/
        val adocao = intent.getBooleanExtra("adocao", false)
        val doacao = intent.getBooleanExtra("doacao", false)

        if(adocao){
            findViewById<TextView>(R.id.titulo).setText("-  ADOÇÃO  -")
        }
        else if (doacao){
            findViewById<TextView>(R.id.titulo).setText("-  DOAÇÃO  -")
        }

        //MANIPULA LISTA DE PETS
        val listaPetAdocaoView = findViewById<RecyclerView>(R.id.listapetadocao)
        listaPetAdocaoView.adapter = ListaPetAdocao(this, GetPetsTESTE().getPetsList(adocao, doacao))//MOCK
        listaPetAdocaoView.layoutManager = LinearLayoutManager(this)

    }
}

//*** LISTA DE VIEW ***//
class ListaPetAdocao(
    private val context: Context,
    private val pets: List<Pet>
) : RecyclerView.Adapter<ListaPetAdocao.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun vincula(pet: Pet, context: Context) {
            val nome = itemView.findViewById<TextView>(R.id.tx_nome)
            nome.text = pet.nome

            val idade = itemView.findViewById<TextView>(R.id.tx_idade)
            idade.text = pet.idade

            val peso = itemView.findViewById<TextView>(R.id.tx_peso)
            peso.text = pet.peso

            // AÇÃO DO BOTÃO [VER PERFIL]
            val btPetPerfil = itemView.findViewById<Button>(R.id.bt_petperfil)
            btPetPerfil.setOnClickListener {
                val intent = Intent(context, PerfilPetActivity::class.java)

                /*envia a variavel adocao para PerfilPetActivity*/
                intent.putExtra("pet_id", pet.id)

                context.startActivity(intent)
            }
        }
    }

    // CRIA O XML DOS OBJETOS
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout._pet, parent, false)
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
