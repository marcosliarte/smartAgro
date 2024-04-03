package br.com.smartagro.activitysfazendas

import AdubacaoAdapter
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.smartagro.R
import com.example.smartagro.LembreteReceiver
import java.text.SimpleDateFormat
import java.util.*

class DetalhesFazendaActivity : AppCompatActivity(), AdubacaoAdapter.AdubacaoAdapterListener {
    private lateinit var adubacaoAdapter: AdubacaoAdapter
    private lateinit var adubacoes: MutableList<String> // Lista para armazenar as adubações

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_fazenda)

        // Obtém o nome da fazenda passado como extra
        val nomeFazenda = intent.getStringExtra("NOME_FAZENDA")

        // Exibe o nome da fazenda no TextView
        findViewById<TextView>(R.id.textViewNomeFazenda).text = nomeFazenda

        // Configura o botão para abrir a nova tela
        findViewById<Button>(R.id.btnAbrirNovaTela).setOnClickListener {
            abrirNovaTela()
        }

        // Inicializa e configura o RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewAdubacoes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Recupera os dados de adubação associados à fazenda específica do banco de dados
        adubacoes = DatabaseHelper(this).getAdubacoes().toMutableList()

        // Inicializa o Adapter e o configura com a lista de adubações
        adubacaoAdapter = AdubacaoAdapter(adubacoes, this)
        recyclerView.adapter = adubacaoAdapter
    }

    override fun onResume() {
        super.onResume()

        // Recarrega os dados de adubação ao retomar a atividade
        adubacoes.clear()
        adubacoes.addAll(DatabaseHelper(this).getAdubacoes())
        adubacaoAdapter.notifyDataSetChanged()
    }

    private fun abrirNovaTela() {
        val intent = Intent(this, CalculoNPK::class.java)
        startActivity(intent)
    }

    override fun onExcluirClick(position: Int) {
        // Remove a adubação da lista
        val adubacao = adubacoes.removeAt(position)
        // Remove a adubação do banco de dados
        DatabaseHelper(this).deleteAdubacao(adubacao)
        // Atualiza a RecyclerView
        adubacaoAdapter.notifyItemRemoved(position)
    }

    override fun onDefinirLembreteClick(dataAdubacao: String) {
        val data = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dataAdubacao)

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, LembreteReceiver::class.java).apply {
            putExtra("DATA_ADUBACAO", dataAdubacao)
            action = "com.example.smartagro.LEMBRETE_ACTION"
        }

        val requestCode = Random().nextInt(1000) // Gera um requestCode aleatório
        val pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance().apply {
            time = data
            set(Calendar.HOUR_OF_DAY, 8) // Define o lembrete para as 8h da manhã
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        Log.d("br.com.smartagro.activitysfazendas.DetalhesFazendaActivity", "Lembrete definido para: $dataAdubacao")

        // Exibir uma mensagem de sucesso
        Toast.makeText(this, "Lembrete definido com sucesso para $dataAdubacao", Toast.LENGTH_SHORT).show()
    }

    override fun onTarefaConcluidaClick(position: Int) {
        // Não é necessário fazer nada aqui, já que a cor dos botões é alterada diretamente no Adapter
    }
}
