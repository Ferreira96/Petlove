package com.example.petlove

class Usuario (
    val id:      Int,
    val nome:    String,
    val email:   String,
    val celular: String,
)

//DADOS PARA TESTE//////////////////////////////////////////////////////////////////////////////////
class GetUsuariosTESTE {
    val usuarios: List<Usuario> = listOf(
        Usuario(0,"Ana Silva"     , "ana@"  , "(11)11111"),
        Usuario(1,"Bruno Dias"    , "bruno@", "(22)22222"),
        Usuario(2,"Renan Braga"   , "renan@", "(33)33333"),
        Usuario(3,"Waldomiro"     , "miro@" , "(44)44444"),
    )

    fun getUsuario(id: Int): Usuario? {
        return usuarios.find { it.id == id }
    }

}
