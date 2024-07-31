package com.example.petlove

class Pet (
    val id:         Int,
    val usuario_id: Int,
    val nome:       String,
    val idade:      String,
    val peso:       String,
    val adocao:     Boolean = false,
    val doacao:     Boolean = false
)

//DADOS PARA TESTE//////////////////////////////////////////////////////////////////////////////////
class GetPetsTESTE {
    val pets: List<Pet> = listOf(
        Pet(0, 0,"Bella"  , "2 anos", "4kg" , true , false),
        Pet(1, 1,"Max"    , "3 anos", "6kg" , true , false),
        Pet(2, 2,"Charlie", "2 anos", "3kg" , true , false),
        Pet(3, 2,"Milo"   , "4 anos", "7kg" , false, true),
        Pet(4, 3,"Luna"   , "2 anos", "5kg" , false, true),
        Pet(5, 3,"Pithuca", "8 anos", "15kg", true , true)
    )

    fun getPetsList(adocao: Boolean, doacao: Boolean): List<Pet> {
        if(adocao){
            val petsAdocaoLista = pets.filter { it.adocao == true }
            return petsAdocaoLista
        }
        else if(doacao){
            val petsDoacaoLista = pets.filter { it.doacao == true }
            return petsDoacaoLista
        }
        return pets
    }

    fun getPet(id: Int): Pet? {
        return pets.find { it.id == id }
    }
}
