import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.smartagro.R

class AdubacaoAdapter(private val adubacoes: MutableList<String>, private val listener: AdubacaoAdapterListener) :
    RecyclerView.Adapter<AdubacaoAdapter.ViewHolder>() {

    private val tarefasConcluidas = BooleanArray(adubacoes.size)

    interface AdubacaoAdapterListener {
        fun onExcluirClick(position: Int)
        fun onDefinirLembreteClick(dataAdubacao: String)
        fun onTarefaConcluidaClick(position: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewDataAdubacao: TextView = itemView.findViewById(R.id.textViewDataAdubacao)
        val btnExcluirAdubacao: Button = itemView.findViewById(R.id.btnExcluirAdubacao)
        val btnDefinirLembrete: Button = itemView.findViewById(R.id.btnDefinirLembrete)
        val btnTarefaConcluida: Button = itemView.findViewById(R.id.btnConcluirTarefa)

        init {
            btnExcluirAdubacao.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onExcluirClick(position)
                }
            }

            btnDefinirLembrete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val dataAdubacao = adubacoes[position]
                    listener.onDefinirLembreteClick(dataAdubacao)
                }
            }

            btnTarefaConcluida.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onTarefaConcluidaClick(position)
                    // Defina a cor de fundo dos botões como cinza após concluir a tarefa
                    btnExcluirAdubacao.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.gray))
                    btnDefinirLembrete.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.gray))
                    btnTarefaConcluida.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.gray))
                    // Atualiza o estado da tarefa como concluída
                    tarefasConcluidas[position] = true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_adubacao, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val adubacao = adubacoes[position]
        holder.textViewDataAdubacao.text = adubacao

        // Define a cor de fundo inicial dos botões com base no estado da tarefa
        if (tarefasConcluidas[position]) {
            holder.btnExcluirAdubacao.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.gray))
            holder.btnDefinirLembrete.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.gray))
            holder.btnTarefaConcluida.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.gray))
        } else {
            holder.btnExcluirAdubacao.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
            holder.btnDefinirLembrete.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
            holder.btnTarefaConcluida.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.blue))
        }
    }

    override fun getItemCount(): Int = adubacoes.size
}
