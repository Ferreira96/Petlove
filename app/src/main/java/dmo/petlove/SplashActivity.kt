package dmo.petlove

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_layout)

        val intent = Intent(this, LoginActivity::class.java)
        val handler = Handler(Looper.getMainLooper()) //A partir do Android API 30 (Android 11), o construtor padrão do Handler é desencorajado. e temos que inserir o Looper.
        handler.postDelayed(Runnable {
            startActivity(intent)
            finish()
        }, 3000)
    }
}