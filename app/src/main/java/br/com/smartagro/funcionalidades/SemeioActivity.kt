package br.com.smartagro

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SemeioActivity : AppCompatActivity() {

    private lateinit var editTextArea: EditText
    private lateinit var editTextPopulacao: EditText
    private lateinit var editTextGerminacao: EditText
    private lateinit var editTextPureza: EditText
    private lateinit var editTextPMG: EditText
    private lateinit var textViewResultado: TextView
    private lateinit var buttonCalcular: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_semeio)

        editTextArea = findViewById(R.id.editTextArea)
        editTextPopulacao = findViewById(R.id.editTextPopulacao)
        editTextGerminacao = findViewById(R.id.editTextGerminacao)
        editTextPureza = findViewById(R.id.editTextPureza)
        editTextPMG = findViewById(R.id.editTextPMG)
        textViewResultado = findViewById(R.id.textViewResultado)
        buttonCalcular = findViewById(R.id.buttonCalcular)

        buttonCalcular.setOnClickListener {
            calcularSemeio()
        }
    }

    private fun calcularSemeio() {
        val area = editTextArea.text.toString().toDouble()
        val populacao = editTextPopulacao.text.toString().toDouble()
        val germinacao = editTextGerminacao.text.toString().toDouble() / 100
        val pureza = editTextPureza.text.toString().toDouble() / 100
        val pmg = editTextPMG.text.toString().toDouble()

        val quantidade = (area * populacao * (100 / germinacao) * (100 / pureza)) / (1000 * pmg)

        // Exibe o resultado na TextView
        textViewResultado.text = "Quantidade de sementes necessárias: $quantidade"
    }
}
