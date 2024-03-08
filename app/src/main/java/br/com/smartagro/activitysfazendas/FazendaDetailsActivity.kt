package br.com.smartagro.activitysfazendas

import Fazenda
import Talhao
import TalhaoAdapter
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.smartagro.R
import java.text.SimpleDateFormat
import java.util.*

class FazendaDetailsActivity : AppCompatActivity(), TalhaoAdapter.TalhaoAdapterListener {

    private lateinit var textViewNomeFazenda: TextView
    private lateinit var editTextCultura: EditText
    private lateinit var editTextArea: EditText
    private lateinit var editTextDataPlantio: EditText
    private lateinit var editTextDataColheita: EditText
    private lateinit var editTextAtividades: EditText
    private lateinit var editTextCustos: EditText
    private lateinit var btnSalvar: Button
    private lateinit var rvTalhoes: RecyclerView

    private lateinit var fazenda: Fazenda
    private val talhoesList = mutableListOf<Talhao>()
    private lateinit var talhaoAdapter: TalhaoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fazenda_details)

        // Inicializando as views
        textViewNomeFazenda = findViewById(R.id.textViewNomeFazenda)
        editTextCultura = findViewById(R.id.editTextCultura)
        editTextArea = findViewById(R.id.editTextArea)
        editTextDataPlantio = findViewById(R.id.editTextDataPlantio)
        editTextDataColheita = findViewById(R.id.editTextDataColheita)
        editTextAtividades = findViewById(R.id.editTextAtividades)
        editTextCustos = findViewById(R.id.editTextCustos)
        btnSalvar = findViewById(R.id.btnSalvar)
        rvTalhoes = findViewById(R.id.rvTalhoes)

        // Obtendo a fazenda da intent
        fazenda = intent.getSerializableExtra("FAZENDA") as? Fazenda ?: return
        textViewNomeFazenda.text = fazenda.nome

        // Configurando o RecyclerView
        rvTalhoes.layoutManager = LinearLayoutManager(this)
        talhaoAdapter = TalhaoAdapter(talhoesList, this)
        rvTalhoes.adapter = talhaoAdapter

        // Configurando o clique do botão salvar
        btnSalvar.setOnClickListener {
            salvarTalhao()
        }
    }

    override fun onEditarClick(position: Int) {
        val talhao = talhoesList[position]
        editTextCultura.setText(talhao.cultura)
        editTextArea.setText(talhao.area.toString())
        editTextDataPlantio.setText(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(talhao.dataPlantio))
        editTextDataColheita.setText(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(talhao.dataPrevistaColheita))
        editTextAtividades.setText(talhao.atividades)
        editTextCustos.setText(talhao.custo.toString())
    }

    override fun onExcluirClick(position: Int) {
        talhoesList.removeAt(position)
        talhaoAdapter.notifyItemRemoved(position)
    }

    private fun salvarTalhao() {
        val cultura = editTextCultura.text.toString()
        val area = editTextArea.text.toString().toDoubleOrNull() ?: 0.0
        val dataPlantio = editTextDataPlantio.text.toString()
        val dataColheita = editTextDataColheita.text.toString()
        val atividades = editTextAtividades.text.toString()
        val custos = editTextCustos.text.toString().toDoubleOrNull() ?: 0.0

        if (cultura.isBlank() || dataPlantio.isBlank() || dataColheita.isBlank()) {
            Toast.makeText(this, "Por favor, preencha todos os campos obrigatórios.", Toast.LENGTH_SHORT).show()
            return
        }

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dataPlantioDate = try { dateFormat.parse(dataPlantio) } catch (e: Exception) { null }
        val dataColheitaDate = try { dateFormat.parse(dataColheita) } catch (e: Exception) { null }

        if (dataPlantioDate == null || dataColheitaDate == null) {
            Toast.makeText(this, "Formato de data inválido. Use 'dd/MM/yyyy'.", Toast.LENGTH_SHORT).show()
            return
        }

        val talhao = Talhao(
            fazendaId = fazenda.id,
            cultura = cultura,
            area = area,
            dataPlantio = dataPlantioDate,
            dataPrevistaColheita = dataColheitaDate,
            atividades = atividades,
            custo = custos
        )

        talhoesList.add(talhao)
        talhaoAdapter.notifyItemInserted(talhoesList.size - 1)
        limparCampos()
    }

    private fun limparCampos() {
        editTextCultura.text.clear()
        editTextArea.text.clear()
        editTextDataPlantio.text.clear()
        editTextDataColheita.text.clear()
        editTextAtividades.text.clear()
        editTextCustos.text.clear()
    }
}
