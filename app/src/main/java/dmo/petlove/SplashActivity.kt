package dmo.petlove

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_layout)

        val intent = Intent(this, LoginActivity::class.java)
        val handler = Handler()
        handler.postDelayed(Runnable {
            startActivity(intent)
            finish()
        }, 4000)
    }
}