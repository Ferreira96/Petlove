package dmo.petlove

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class LoginActivity : AppCompatActivity() {

    lateinit var btnNewUser: TextView
    lateinit var toolbar: Toolbar
    lateinit var txtTitle: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)

        //Segue para  tela "MainActivity" após login (ainda não está validando se o usuário está no banco)
        Handler(Looper.getMainLooper()).post{
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }


    //Linkando TxtView_Sem_Cadastro à Tela de Registro pela ação OnClick
    private fun setBtnCadastro() {
        btnNewUser = findViewById(R.id.TxtView_Sem_Cadastro)
        btnNewUser.setOnClickListener(View.OnClickListener {
            val intent = Intent(
                this@LoginActivity,
                RegisterActivity::class.java
            )
            startActivity(intent)
        })
    }

    //Linkando TxtView_Sem_Cadastro à Tela de Esqueci Minha Senha pela ação OnClick - Verificar se ainda é útil após o uso do Firebase
   /* private fun setBtnEsqueciSenha() {
        btnNewUser = findViewById(R.id.TxtView_Esqueci_Senha)
        btnNewUser.setOnClickListener(View.OnClickListener {
            val intent = Intent(
                this@LoginActivity,
                ForgotPassword::class.java
            )
            startActivity(intent)
        })
    }
}*/

}
