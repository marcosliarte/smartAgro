package br.com.smartagro.activitysfazendas

import Fazenda
import FazendaAdapter
import FazendaDAO
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.smartagro.R

class MainActivityFazendas : AppCompatActivity() {

    private lateinit var editTextNome: EditText
    private lateinit var editTextTamanho: EditText
    private lateinit var editTextLocalizacao: EditText
    private lateinit var btnSalvar: Button
    private lateinit var rvFazendas: RecyclerView

    private lateinit var fazendaDAO: FazendaDAO
    private var fazendaAtual: Fazenda? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_fazendas_talhoes)

        editTextNome = findViewById(R.id.editTextNome)
        editTextTamanho = findViewById(R.id.editTextTamanho)
        editTextLocalizacao = findViewById(R.id.editTextLocalizacao)
        btnSalvar = findViewById(R.id.btnSalvar)
        rvFazendas = findViewById(R.id.rvFazendas)

        fazendaDAO = FazendaDAO(this)

        btnSalvar.setOnClickListener {
            salvarOuAtualizarFazenda()
        }

        configurarRecyclerView()
    }

    private fun configurarRecyclerView() {
        rvFazendas.layoutManager = LinearLayoutManager(this)
        atualizarListaFazendas()
    }

    private fun salvarOuAtualizarFazenda() {
        val nome = editTextNome.text.toString()
        val tamanho = editTextTamanho.text.toString().toDoubleOrNull() ?: 0.0
        val localizacao = editTextLocalizacao.text.toString()

        if (fazendaAtual == null) {
            fazendaDAO.inserirFazenda(nome, tamanho, localizacao)
        } else {
            fazendaAtual?.let {
                fazendaDAO.atualizarFazenda(it.id, nome, tamanho, localizacao)
            }
        }

        limparCampos()
        atualizarListaFazendas()
    }

    private fun atualizarListaFazendas() {
        val fazendas = fazendaDAO.buscarFazendas()
        rvFazendas.adapter = FazendaAdapter(fazendas, object : FazendaAdapter.FazendaAdapterListener {
            override fun onEditarClick(position: Int) {
                editarFazenda(fazendas[position])
            }

            override fun onExcluirClick(position: Int) {
                deletarFazenda(fazendas[position])
            }

            override fun onVisualizarClick(position: Int) {
                visualizarFazenda(fazendas[position])
            }
        })
    }

    private fun editarFazenda(fazenda: Fazenda) {
        editTextNome.setText(fazenda.nome)
        editTextTamanho.setText(fazenda.tamanho.toString())
        editTextLocalizacao.setText(fazenda.localizacao)
        fazendaAtual = fazenda
    }

    private fun deletarFazenda(fazenda: Fazenda) {
        fazendaDAO.excluirFazenda(fazenda.id)
        atualizarListaFazendas()
    }

    private fun visualizarFazenda(fazenda: Fazenda) {
        val intent = Intent(this, FazendaDetailsActivity::class.java)
        // Certifique-se de que a classe Fazenda implementa Serializable
        intent.putExtra("FAZENDA", fazenda)
        startActivity(intent)
    }


    private fun limparCampos() {
        editTextNome.setText("")
        editTextTamanho.setText("")
        editTextLocalizacao.setText("")
        fazendaAtual = null
    }
}
