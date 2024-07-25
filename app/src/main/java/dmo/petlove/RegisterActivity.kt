package dmo.petlove

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegisterActivity : AppCompatActivity() {

    lateinit var btnLoginReturn: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.register_layout)

        //Mandando tela de registro para principal, após clicar no botão
        Handler(Looper.getMainLooper()).post{
            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

        //Linkando TxtView_RetornaLogin à Tela de Login pela ação OnClick
        private fun setBtnRetornaLogin() {
            btnLoginReturn = findViewById(R.id.TxtView_RetornaLogin)
            btnLoginReturn.setOnClickListener(View.OnClickListener {
                val intent = Intent(
                    this@RegisterActivity,
                    LoginActivity::class.java
                )
                startActivity(intent)
            })
        }

}