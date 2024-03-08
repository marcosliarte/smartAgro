package br.com.smartagro

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AdubacaoActivity : AppCompatActivity() {

    private lateinit var editTextArea: EditText
    private lateinit var editTextTeorNutrientes: EditText
    private lateinit var editTextEficienciaAdubo: EditText
    private lateinit var editTextTeorNutrientesAdubo: EditText
    private lateinit var buttonCalcular: Button
    private lateinit var textViewResultado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adubacao)

        editTextArea = findViewById(R.id.editTextArea)
        editTextTeorNutrientes = findViewById(R.id.editTextTeorNutrientes)
        editTextEficienciaAdubo = findViewById(R.id.editTextEficienciaAdubo)
        editTextTeorNutrientesAdubo = findViewById(R.id.editTextTeorNutrientesAdubo)
        buttonCalcular = findViewById(R.id.buttonCalcular)
        textViewResultado = findViewById(R.id.textViewResultado)

        buttonCalcular.setOnClickListener {
            calcularAdubacao()
        }
    }

    private fun calcularAdubacao() {
        val area = editTextArea.text.toString().toDouble()
        val teorNutrientes = editTextTeorNutrientes.text.toString().toDouble()
        val eficienciaAdubo = editTextEficienciaAdubo.text.toString().toDouble()
        val teorNutrientesAdubo = editTextTeorNutrientesAdubo.text.toString().toDouble()

        // Calcula a quantidade de adubo necessária
        val quantidadeAdubo = (area * teorNutrientes * (100 - eficienciaAdubo)) / teorNutrientesAdubo

        // Exibe o resultado na TextView
        textViewResultado.text = String.format("%.2f", quantidadeAdubo) + " kg de adubo necessário"
    }
}
