package br.edu.utfpr.myeconomy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var btHome : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btHome = findViewById(R.id.btHome)

        btHome.setOnClickListener {
            btHomeOnClick()
        }
    }

    private fun btHomeOnClick() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity( intent )
    }
}