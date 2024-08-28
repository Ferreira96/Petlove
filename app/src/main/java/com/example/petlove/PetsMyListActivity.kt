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
import com.example.petlove.repository.getPetImg
import com.example.petlove.repository.getPets
import com.example.petlove.repository.getUsuarioPorEmail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MeusPetsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pets_my_list)

        //BOTÃO [NOVO PET]
        findViewById<Button>(R.id.bt_pets_my).setOnClickListener {
            startActivity(Intent(this, FormPetActivity::class.java))
        }

        //SESSÃO
        val sessao = getSharedPreferences("sessao", Context.MODE_PRIVATE)
        val userEmail = sessao.getString("USER_EMAIL", null)

        //CONFIGURA LISTA <_pet_my>
        CoroutineScope(Dispatchers.Main).launch {

            //FILTRA ID USUARIO
            var id_usuario = 0
            if (userEmail != null) {
                val usuario = getUsuarioPorEmail(userEmail)
                id_usuario = usuario?.id ?: 0
            }
            var pets = getPets().filter { it.usuario_id == id_usuario }

            /*CONFIGURA LISTA <_pet_my>*/
            val listaPetAdocaoView = findViewById<RecyclerView>(R.id.list_pets_my)
            listaPetAdocaoView.adapter       = ListaMeusPets(this@MeusPetsActivity, pets)
            listaPetAdocaoView.layoutManager = LinearLayoutManager(this@MeusPetsActivity)
        }
    }
}

//CONFIGURA LISTA <_pet_my>
class ListaMeusPets(
    private val context: Context,
    private val pets: List<Pet>
    ) : RecyclerView.Adapter<ListaMeusPets.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun vincula(pet: Pet, context: Context) {

            //CARREGA DADOS DO FIRESTORE
            val nome = itemView.findViewById<TextView>(R.id.tx_pet_operations_nome)
            nome.text = pet.nome

            val idade = itemView.findViewById<TextView>(R.id.tx_pet_operations_idade)
            idade.text = pet.idade.toString() + " Anos"

            val peso = itemView.findViewById<TextView>(R.id.tx_pet_operations_peso)
            peso.text = pet.peso.toString() + " Kg"

            //CARREGA IMAGEM DO STORAGE
            CoroutineScope(Dispatchers.Main).launch {
                val img_pet_user: ImageView = itemView.findViewById(R.id.img_pet_operations)
                val petImageUri = getPetImg(pet.id)
                if (petImageUri != null) {
                    Glide.with(itemView)
                        .load(petImageUri)
                        .into(img_pet_user)
                }
            }

            //BOTAO [EDITAR]
            val botaoEditar = itemView.findViewById<ImageView>(R.id.bt_pet_operations_editar)
            botaoEditar.setOnClickListener {
                val intent = Intent(context, FormPetActivity::class.java)
                intent.putExtra("petToUpdate_id", pet.id)
                context.startActivity(intent)
            }

            //BOTAO [EXCLUIR]
            val botaoExcluir = itemView.findViewById<ImageView>(R.id.bt_pet_operations_excluir)
            botaoExcluir.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    deletePet(pet.id)
                }
                val intent = Intent(context, MeusPetsActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    //OPERAÇOES BASICAS RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout._pet_operations, parent, false)
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
