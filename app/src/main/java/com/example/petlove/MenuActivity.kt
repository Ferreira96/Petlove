package com.example.petlove

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.view.MenuItem
import android.widget.Button

class MenuActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Configurando o DrawerLayout e NavigationView
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_meus_pets -> {
                    startActivity(Intent(this, MeusPetsActivity::class.java))
                }
                R.id.nav_adocao -> {
                    val intent = Intent(this, AdocaoEDoacaoActivity::class.java)
                    intent.putExtra("adocao", true)
                    startActivity(intent)
                }
                R.id.nav_doacao -> {
                    val intent = Intent(this, AdocaoEDoacaoActivity::class.java)
                    intent.putExtra("doacao", true)
                    startActivity(intent)
                }
            }
            drawerLayout.closeDrawers()
            true
        }

        //AÇÃO DO BOTÃO [MEUS PETS].
        findViewById<Button>(R.id.bt_menu_meus).setOnClickListener {
            startActivity(Intent(this, MeusPetsActivity::class.java))
        }

        //AÇÃO DO BOTÃO [ADOÇÃO]
        findViewById<Button>(R.id.bt_menu_adocoes).setOnClickListener {
            val intent = Intent(this, AdocaoEDoacaoActivity::class.java)
            intent.putExtra("adocao", true)/*envia var*/
            startActivity(intent)
        }

        //AÇÃO DO BOTÃO [DOAÇÃO]
        findViewById<Button>(R.id.bt_menu_doacoes).setOnClickListener {
            val intent = Intent(this, AdocaoEDoacaoActivity::class.java)
            intent.putExtra("doacao", true)/*envia var*/
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}