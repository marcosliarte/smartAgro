package br.com.smartagro

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CalculoPulverizadorActivity : AppCompatActivity() {

    private lateinit var editTextLargura: EditText
    private lateinit var editTextComprimento: EditText
    private lateinit var editTextAltura: EditText
    private lateinit var editTextDensidade: EditText
    private lateinit var editTextEficienciaPulverizador: EditText
    private lateinit var buttonCalcular: Button
    private lateinit var textViewResultado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculo_pulverizador)

        editTextLargura = findViewById(R.id.editTextLargura)
        editTextComprimento = findViewById(R.id.editTextComprimento)
        editTextAltura = findViewById(R.id.editTextAltura)
        editTextDensidade = findViewById(R.id.editTextDensidade)
        editTextEficienciaPulverizador = findViewById(R.id.editTextEficienciaPulverizador)
        buttonCalcular = findViewById(R.id.buttonCalcular)
        textViewResultado = findViewById(R.id.textViewResultado)

        buttonCalcular.setOnClickListener {
            calcularVolumePulverizador()
        }
    }

    private fun calcularVolumePulverizador() {
        val largura = editTextLargura.text.toString().toDouble()
        val comprimento = editTextComprimento.text.toString().toDouble()
        val altura = editTextAltura.text.toString().toDouble()
        val densidade = editTextDensidade.text.toString().toDouble()
        val eficienciaPulverizador = editTextEficienciaPulverizador.text.toString().toDouble()

        // Calcula o volume de líquido necessário
        val volume = largura * comprimento * altura * densidade * (1 / eficienciaPulverizador)

        // Exibe o resultado na TextView
        textViewResultado.text = String.format("%.2f", volume) + " litros de líquido necessário"
    }
}
