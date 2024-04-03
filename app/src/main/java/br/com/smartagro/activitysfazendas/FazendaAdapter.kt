import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.smartagro.R

class FazendaAdapter(private val fazendas: List<Fazenda>, private val listener: FazendaAdapterListener) :
    RecyclerView.Adapter<FazendaAdapter.ViewHolder>() {

    interface FazendaAdapterListener {
        fun onEditarClick(position: Int)
        fun onVisualizarClick(position: Int)
        fun onExcluirClick(position: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeTextView: TextView = itemView.findViewById(R.id.textViewNomeFazenda)
        val tamanhoTextView: TextView = itemView.findViewById(R.id.textViewArea)
        val localizacaoTextView: TextView = itemView.findViewById(R.id.textViewLocalizacaoFazenda)
        val editarButton: Button = itemView.findViewById(R.id.btnEditarFazenda)
        val visualizarButton: Button = itemView.findViewById(R.id.btnVisualizarFazenda)
        val excluirButton: Button = itemView.findViewById(R.id.btnDeletarFazenda)

        init {
            editarButton.setOnClickListener { listener.onEditarClick(adapterPosition) }
            visualizarButton.setOnClickListener { listener.onVisualizarClick(adapterPosition) }
            excluirButton.setOnClickListener { listener.onExcluirClick(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fazenda, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fazenda = fazendas[position]
        holder.nomeTextView.text = fazenda.nome
        holder.tamanhoTextView.text = "${fazenda.tamanho} ha"
        holder.localizacaoTextView.text = fazenda.localizacao
    }

    override fun getItemCount(): Int = fazendas.size
}
