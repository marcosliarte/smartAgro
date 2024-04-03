package br.com.smartagro.activitysfazendas

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.smartagro.R
import java.text.SimpleDateFormat
import java.util.*

class CalculoNPK : AppCompatActivity() {
    // Mapeamento de culturas para valores de NPK (necessidade anual) e pesos
    private val npkMap = mapOf(
        "Abacate" to arrayOf(45.0, 21.0, 75.0, 0.8, 0.5, 0.7),
        "Abobrinha" to arrayOf(4.0, 14.0, 8.0, 0.3, 0.3, 0.4),
        "Bananeira" to arrayOf(160.0, 40.0, 450.0, 0.9, 0.7, 0.8),
        "Batata-Doce" to arrayOf(50.0, 35.0, 70.0, 1.5, 1.0, 1.3),
        "Berinjela" to arrayOf(100.0, 30.0, 100.0, 1.4, 0.8, 1.2),
        "Cacauzeiro" to arrayOf(60.0, 30.0, 80.0, 1.7, 1.2, 1.4),
        "Cajueiro" to arrayOf(100.0, 50.0, 125.0, 1.5, 1.1, 1.3),
        "Carambola" to arrayOf(40.0, 20.0, 60.0, 1.2, 0.8, 1.0),
        "Coco" to arrayOf(50.0, 30.0, 80.0, 1.7, 1.1, 1.4),
        "Coqueiro" to arrayOf(50.0, 30.0, 80.0, 1.7, 1.1, 1.4),
        "Couve-Flor" to arrayOf(80.0, 40.0, 100.0, 1.2, 0.8, 1.0),
        "Feijão-De-Corda" to arrayOf(30.0, 20.0, 40.0, 1.4, 0.9, 1.2),
        "Goiaba" to arrayOf(40.0, 20.0, 70.0, 1.3, 0.8, 1.1),
        "Graviola" to arrayOf(60.0, 30.0, 80.0, 1.6, 0.9, 1.3),
        "Mamão" to arrayOf(40.0, 20.0, 70.0, 1.6, 1.0, 1.3),
        "Mandioquinha" to arrayOf(40.0, 20.0, 70.0, 1.4, 1.1, 1.2),
        "Maracujá" to arrayOf(40.0, 20.0, 70.0, 1.5, 1.0, 1.3),
        "Melancia" to arrayOf(40.0, 20.0, 70.0, 1.8, 1.1, 1.5),
        "Melão" to arrayOf(40.0, 20.0, 70.0, 1.6, 1.0, 1.3),
        "Pepino" to arrayOf(40.0, 20.0, 70.0, 1.1, 0.7, 1.2),
        "Pimentão" to arrayOf(40.0, 20.0, 70.0, 1.4, 0.9, 1.3),
        "Salsa" to arrayOf(30.0, 20.0, 40.0, 1.1, 0.7, 1.0),
        "Salsão" to arrayOf(40.0, 20.0, 70.0, 1.2, 0.8, 1.0),
        "Alface" to arrayOf(50.0, 20.0, 70.0, 1.1, 0.6, 0.9),
        "Alho" to arrayOf(40.0, 20.0, 70.0, 1.5, 1.2, 1.3),
        "Cebola" to arrayOf(40.0, 20.0, 70.0, 1.4, 1.0, 1.1),
        "Cenoura" to arrayOf(60.0, 40.0, 80.0, 1.3, 0.9, 1.0),
        "Feijão" to arrayOf(40.0, 20.0, 70.0, 1.3, 0.9, 1.1),
        "Açaí" to arrayOf(80.0, 40.0, 100.0, 1.5, 1.0, 1.2)

    )

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculo_npk)

        databaseHelper = DatabaseHelper(this)

        val editTextNitrogenio = findViewById<EditText>(R.id.editTextNitrogenio)
        val editTextFosforo = findViewById<EditText>(R.id.editTextFosforo)
        val editTextPotassio = findViewById<EditText>(R.id.editTextPotassio)
        val btnCalcularIntervalo = findViewById<Button>(R.id.btnCalcularIntervalo)
        val btnExcluirPlano = findViewById<Button>(R.id.btnExcluirPlano)
        val textViewDataAdubacao1 = findViewById<TextView>(R.id.textViewDataAdubacao1)
        val textViewDataAdubacao2 = findViewById<TextView>(R.id.textViewDataAdubacao2)
        val textViewDataAdubacao3 = findViewById<TextView>(R.id.textViewDataAdubacao3)
        val textViewDataAdubacao4 = findViewById<TextView>(R.id.textViewDataAdubacao4)
        val spinnerCultura = findViewById<Spinner>(R.id.spinnerCultura)

        // Definir as culturas no Spinner
        val culturas = resources.getStringArray(R.array.culturas)
        spinnerCultura.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, culturas)

        btnCalcularIntervalo.setOnClickListener {
            val cultura = spinnerCultura.selectedItem.toString()
            val npkValuesCultura = npkMap[cultura] ?: arrayOf(0.0, 0.0, 0.0, 0.5, 0.5, 0.5) // Valores padrão se a cultura não for encontrada
            val pesoNitrogenio = npkValuesCultura[3]
            val pesoFosforo = npkValuesCultura[4]
            val pesoPotassio = npkValuesCultura[5]

            // Obter os valores de NPK inseridos pelo usuário
            val npkValuesUser = arrayOf(
                editTextNitrogenio.text.toString().toDoubleOrNull() ?: 0.0,
                editTextFosforo.text.toString().toDoubleOrNull() ?: 0.0,
                editTextPotassio.text.toString().toDoubleOrNull() ?: 0.0
            )

            // Calcular os valores de NPK totais (cultura + usuário)
            val npkValuesTotal = DoubleArray(3) { index ->
                npkValuesCultura[index] / 12 * pesoNitrogenio + npkValuesUser[index] * pesoNitrogenio +
                        npkValuesCultura[index] / 12 * pesoFosforo + npkValuesUser[index] * pesoFosforo +
                        npkValuesCultura[index] / 12 * pesoPotassio + npkValuesUser[index] * pesoPotassio
            }

            if (npkValuesTotal.all { it >= 0 }) {
                val datasAdubacao = calcularDatasAdubacao(npkValuesTotal)
                if (datasAdubacao.isNotEmpty()) {
                    textViewDataAdubacao1.apply {
                        visibility = View.VISIBLE
                        text = formatarData(datasAdubacao.getOrNull(0))
                    }
                    textViewDataAdubacao2.apply {
                        visibility = View.VISIBLE
                        text = formatarData(datasAdubacao.getOrNull(1))
                    }
                    textViewDataAdubacao3.apply {
                        visibility = View.VISIBLE
                        text = formatarData(datasAdubacao.getOrNull(2))
                    }
                    textViewDataAdubacao4.apply {
                        visibility = View.VISIBLE
                        text = formatarData(datasAdubacao.getOrNull(3))
                    }

                    // Adicionar os dados ao banco de dados
                    for (data in datasAdubacao) {
                        databaseHelper.addAdubacao("$data - $cultura")
                    }
                } else {
                    exibirMensagem("Não foi possível calcular as datas de adubação.")
                }
            } else {
                exibirMensagem("Os valores de NPK devem ser números positivos.")
            }
        }

        btnExcluirPlano.setOnClickListener {
            editTextNitrogenio.text.clear()
            editTextFosforo.text.clear()
            editTextPotassio.text.clear()
            textViewDataAdubacao1.text = ""
            textViewDataAdubacao2.text = ""
            textViewDataAdubacao3.text = ""
            textViewDataAdubacao4.text = ""
            textViewDataAdubacao1.visibility = View.GONE
            textViewDataAdubacao2.visibility = View.GONE
            textViewDataAdubacao3.visibility = View.GONE
            textViewDataAdubacao4.visibility = View.GONE
        }
    }

    private fun calcularDatasAdubacao(npkValues: DoubleArray): List<String> {
        val datasAdubacao = mutableListOf<String>()
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val frequenciaAdubacao = calcularFrequenciaAdubacao(npkValues)

        val diasRestantesNoAno = DIAS_NO_ANO - calendar.get(Calendar.DAY_OF_YEAR)
        val quantidadeAdubacoesRestantes = diasRestantesNoAno / frequenciaAdubacao

        for (j in 1..quantidadeAdubacoesRestantes) {
            calendar.add(Calendar.DAY_OF_YEAR, frequenciaAdubacao)
            datasAdubacao.add(dateFormat.format(calendar.time))
        }

        return datasAdubacao
    }

    private fun calcularFrequenciaAdubacao(npkValues: DoubleArray): Int {
        val score = npkValues.sum()

        return when {
            score < 50 -> 90 // Adubar a cada 90 dias para culturas que precisam menos
            score < 100 -> 60 // Adubar a cada 60 dias para culturas de necessidade média
            else -> 30 // Adubar a cada 30 dias para culturas que precisam mais
        }
    }

    private fun formatarData(data: String?): String {
        return data?.let {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dateFormat.format(dateFormat.parse(data))
        } ?: "N/A"
    }

    private fun exibirMensagem(mensagem: String) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val DIAS_NO_ANO = 365
    }
}
