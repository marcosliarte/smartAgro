package br.com.smartagro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import br.com.smartagro.funcionalidades.CalculoPulverizadorActivity

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_semeio)

        val buttonAdubacao: Button = findViewById(R.id.buttonAdubacao)
        val buttonCalculoPulverizador: Button = findViewById(R.id.buttonCalculoPulverizador)


        buttonAdubacao.setOnClickListener {
            startActivity(Intent(this, br.com.smartagro.AdubacaoActivity::class.java))
        }

        buttonCalculoPulverizador.setOnClickListener {
            startActivity(Intent(this, CalculoPulverizadorActivity::class.java))
        }
    }
}
