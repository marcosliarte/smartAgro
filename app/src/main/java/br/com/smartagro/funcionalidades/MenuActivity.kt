package br.com.smartagro

import br.com.smartagro.AdubacaoActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_semeio)

        val buttonSemeio: Button = findViewById(R.id.buttonSemeio)
        val buttonAdubacao: Button = findViewById(R.id.buttonAdubacao)
        val buttonCalculoPulverizador: Button = findViewById(R.id.buttonCalculoPulverizador)

        buttonSemeio.setOnClickListener {
            startActivity(Intent(this, br.com.smartagro.SemeioActivity::class.java))
        }

        buttonAdubacao.setOnClickListener {
            startActivity(Intent(this, br.com.smartagro.AdubacaoActivity::class.java))
        }

        buttonCalculoPulverizador.setOnClickListener {
            startActivity(Intent(this, br.com.smartagro.CalculoPulverizadorActivity::class.java))
        }
    }
}
