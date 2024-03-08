import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.smartagro.R
import java.text.SimpleDateFormat
import java.util.*

class TalhaoAdapter(private val talhoes: List<Talhao>, private val listener: TalhaoAdapterListener) :
    RecyclerView.Adapter<TalhaoAdapter.ViewHolder>() {

    interface TalhaoAdapterListener {
        fun onEditarClick(position: Int)
        fun onExcluirClick(position: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val culturaTextView: TextView = itemView.findViewById(R.id.textViewCultura)
        val areaTextView: TextView = itemView.findViewById(R.id.textViewArea)
        val dataPlantioTextView: TextView = itemView.findViewById(R.id.textViewDataPlantio)
        val dataPrevistaColheitaTextView: TextView = itemView.findViewById(R.id.textViewDataColheita)
        val atividadesTextView: TextView = itemView.findViewById(R.id.textViewAtividades)
        val custoTextView: TextView = itemView.findViewById(R.id.textViewCustos)
        val editarButton: Button = itemView.findViewById(R.id.btnEditar)
        val excluirButton: Button = itemView.findViewById(R.id.btnExcluir)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_talhao, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val talhao = talhoes[position]
        holder.culturaTextView.text = talhao.cultura
        holder.areaTextView.text = "${talhao.area} ha"
        holder.dataPlantioTextView.text = SimpleDateFormat("dd/MM/yyyy").format(talhao.dataPlantio)
        holder.dataPrevistaColheitaTextView.text = SimpleDateFormat("dd/MM/yyyy").format(talhao.dataPrevistaColheita)
        holder.atividadesTextView.text = talhao.atividades
        holder.custoTextView.text = "R$ ${String.format("%.2f", talhao.custo)}"

        holder.editarButton.setOnClickListener { listener.onEditarClick(position) }
        holder.excluirButton.setOnClickListener { listener.onExcluirClick(position) }
    }

    override fun getItemCount(): Int = talhoes.size
}
