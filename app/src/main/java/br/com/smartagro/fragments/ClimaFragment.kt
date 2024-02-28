import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import br.com.smartagro.clima.Conexao
import br.com.smartagro.clima.ConsumirXML
import br.com.smartagro.clima.Previsao
import br.com.smartagro.R
import br.com.smartagro.clima.SiglaDescricao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.text.DateFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class ClimaFragment : Fragment() {
    private val previsoesList = ArrayList<Previsao>()
    private var cidadeId: String? = null
    private var cidadeNomeUF: String? = null
    private var indexAtual = 0
    private lateinit var imgTempo: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_clima, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TextView para mostrar a cidade
        val lblCidade = view.findViewById<TextView>(R.id.lblCidade)

        // Obter usuário atual
        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid

            // Inicializar o Firestore
            val db = FirebaseFirestore.getInstance()

            // Referenciar a coleção de cidades do usuário atual
            val cidadeRef: CollectionReference = db.collection("usuarios").document(userId)
                .collection("cidade")

            // Consultar a primeira cidade da coleção
            cidadeRef.limit(1).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document: QueryDocumentSnapshot in task.result!!) {
                            cidadeNomeUF =
                                document.getString("nome") + " - " + document.getString("uf")
                            cidadeId = document.getString("id")

                            lblCidade.text = cidadeNomeUF
                            buscarPrevisao()
                            break // Para apenas na primeira cidade encontrada
                        }
                    } else {
                        Log.d("Cidade", "Falha ao recuperar os dados da cidade", task.exception)
                    }
                }
        }

        val card0 = view.findViewById<View>(R.id.card0)
        card0.setOnClickListener {
            indexAtual = 0
            buscarPrevisao();
        }

        val card1 = view.findViewById<View>(R.id.card1)
        card1.setOnClickListener {
            indexAtual = 1
            buscarPrevisao();
        }

        val card2 = view.findViewById<View>(R.id.card2)
        card2.setOnClickListener {
            indexAtual = 2
            buscarPrevisao();
        }

        val card3 = view.findViewById<View>(R.id.card3)
        card3.setOnClickListener {
            indexAtual = 3
            buscarPrevisao();
        }

        val card4 = view.findViewById<View>(R.id.card4)
        card4.setOnClickListener {
            indexAtual = 4
            buscarPrevisao();
        }

        val card5 = view.findViewById<View>(R.id.card5)
        card5.setOnClickListener {
            indexAtual = 5
            buscarPrevisao();
        }

        val btnCompartilhar = view.findViewById<View>(R.id.btnCompartilhar)
        btnCompartilhar.setOnClickListener {
            compartilhar(view)
        }

    }

    private fun buscarPrevisao() {
        try {
            val url = "http://servicos.cptec.inpe.br/XML/cidade/7dias/$cidadeId/previsao.xml"
            // String url = "https://gist.githubusercontent.com/kleber0a0m0/738376b6d7616702448ace751425e05a/raw/2cec2d51ea07cfea40adb576f0460e397bf85d58/inpe.xml";
            Tarefa().execute(url)
        } catch (e: Exception) {
            e.message?.let { Log.e("Erro", it) }
        }
    }

    inner class Tarefa : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg strings: String): String {
            val dados = Conexao.getDados(strings[0])
            Log.d("Dados", "Dados: $dados")
            return dados
        }

        override fun onPostExecute(s: String) {
            previsoesList.clear()
            previsoesList.addAll(ConsumirXML.getPrevisao(s))
            val txtDia = view?.findViewById<TextView>(R.id.txtDia)
            val txtDiaMes = view?.findViewById<TextView>(R.id.txtDiaMes)
            val txtTempo = view?.findViewById<TextView>(R.id.txtTempo)
            val txtUV = view?.findViewById<TextView>(R.id.txtUV)
            val txtMax = view?.findViewById<TextView>(R.id.txtMax)
            val txtMin = view?.findViewById<TextView>(R.id.txtMin)

            txtDia?.text = obterDiaDaSemana(previsoesList[indexAtual].dia)
            txtDiaMes?.text = obterDiaMes(previsoesList[indexAtual].dia)
            txtTempo?.text = SiglaDescricao.converterSiglaParaDescricao(previsoesList[indexAtual].tempo)

            imgTempo = view?.findViewById(R.id.imgTempo) ?: return
            val resourceId =
                resources.getIdentifier(previsoesList[indexAtual].tempo, "drawable", requireActivity().packageName)
            imgTempo.setImageResource(resourceId)

            txtUV?.text = obterClassificacaoUV(previsoesList[indexAtual].iuv)
            txtMax?.text = "${previsoesList[indexAtual].maxima} ºC"
            txtMin?.text = "${previsoesList[indexAtual].minima} ºC"

            atualizarCards()
        }
    }

    private fun atualizarCards() {
        // Card 01
        val txtDiaCard_01 = view?.findViewById<TextView>(R.id.txtDiaCard_01)
        val txtMesCard_01 = view?.findViewById<TextView>(R.id.txtMesCard_01)
        val txtTempoCard_01 = view?.findViewById<TextView>(R.id.txtTempoCard_01)
        val txtUvCard_01 = view?.findViewById<TextView>(R.id.txtUvCard_01)
        val imgTempoCard_01 = view?.findViewById<ImageView>(R.id.imgTempoCard_01)

        txtDiaCard_01?.text = obterDia(previsoesList[0].dia)
        txtMesCard_01?.text = obterMes(previsoesList[0].dia)
        txtTempoCard_01?.text = SiglaDescricao.converterSiglaParaDescricao(previsoesList[0].tempo)
        txtUvCard_01?.text = obterClassificacaoUV(previsoesList[0].iuv)

        val resourceIdTempoCard_01 = resources.getIdentifier(
            previsoesList[0].tempo,
            "drawable",
            requireActivity().packageName
        )
        imgTempoCard_01?.setImageResource(resourceIdTempoCard_01)

        // Card 02
        val txtDiaCard_02 = view?.findViewById<TextView>(R.id.txtDiaCard_02)
        val txtMesCard_02 = view?.findViewById<TextView>(R.id.txtMesCard_02)
        val txtTempoCard_02 = view?.findViewById<TextView>(R.id.txtTempoCard_02)
        val txtUvCard_02 = view?.findViewById<TextView>(R.id.txtUvCard_02)
        val imgTempoCard_02 = view?.findViewById<ImageView>(R.id.imgTempoCard_02)

        txtDiaCard_02?.text = obterDia(previsoesList[1].dia)
        txtMesCard_02?.text = obterMes(previsoesList[1].dia)
        txtTempoCard_02?.text = SiglaDescricao.converterSiglaParaDescricao(previsoesList[1].tempo)
        txtUvCard_02?.text = obterClassificacaoUV(previsoesList[1].iuv)

        val resourceIdTempoCard_02 = resources.getIdentifier(
            previsoesList[1].tempo,
            "drawable",
            requireActivity().packageName
        )
        imgTempoCard_02?.setImageResource(resourceIdTempoCard_02)

        // Repita o mesmo padrão para os outros cards (03 a 06) da mesma forma que foi feito para o Card 02.

        // Card 03
        val txtDiaCard_03 = view?.findViewById<TextView>(R.id.txtDiaCard_03)
        val txtMesCard_03 = view?.findViewById<TextView>(R.id.txtMesCard_03)
        val txtTempoCard_03 = view?.findViewById<TextView>(R.id.txtTempoCard_03)
        val txtUvCard_03 = view?.findViewById<TextView>(R.id.txtUvCard_03)
        val imgTempoCard_03 = view?.findViewById<ImageView>(R.id.imgTempoCard_03)

        txtDiaCard_03?.text = obterDia(previsoesList[2].dia)
        txtMesCard_03?.text = obterMes(previsoesList[2].dia)
        txtTempoCard_03?.text = SiglaDescricao.converterSiglaParaDescricao(previsoesList[2].tempo)
        txtUvCard_03?.text = obterClassificacaoUV(previsoesList[2].iuv)

        val resourceIdTempoCard_03 = resources.getIdentifier(
            previsoesList[2].tempo,
            "drawable",
            requireActivity().packageName
        )
        imgTempoCard_03?.setImageResource(resourceIdTempoCard_03)

        // Card 04
        val txtDiaCard_04 = view?.findViewById<TextView>(R.id.txtDiaCard_04)
        val txtMesCard_04 = view?.findViewById<TextView>(R.id.txtMesCard_04)
        val txtTempoCard_04 = view?.findViewById<TextView>(R.id.txtTempoCard_04)
        val txtUvCard_04 = view?.findViewById<TextView>(R.id.txtUvCard_04)
        val imgTempoCard_04 = view?.findViewById<ImageView>(R.id.imgTempoCard_04)

        txtDiaCard_04?.text = obterDia(previsoesList[3].dia)
        txtMesCard_04?.text = obterMes(previsoesList[3].dia)
        txtTempoCard_04?.text = SiglaDescricao.converterSiglaParaDescricao(previsoesList[3].tempo)
        txtUvCard_04?.text = obterClassificacaoUV(previsoesList[3].iuv)

        val resourceIdTempoCard_04 = resources.getIdentifier(
            previsoesList[3].tempo,
            "drawable",
            requireActivity().packageName
        )
        imgTempoCard_04?.setImageResource(resourceIdTempoCard_04)

        // Card 05
        val txtDiaCard_05 = view?.findViewById<TextView>(R.id.txtDiaCard_05)
        val txtMesCard_05 = view?.findViewById<TextView>(R.id.txtMesCard_05)
        val txtTempoCard_05 = view?.findViewById<TextView>(R.id.txtTempoCard_05)
        val txtUvCard_05 = view?.findViewById<TextView>(R.id.txtUvCard_05)
        val imgTempoCard_05 = view?.findViewById<ImageView>(R.id.imgTempoCard_05)

        txtDiaCard_05?.text = obterDia(previsoesList[4].dia)
        txtMesCard_05?.text = obterMes(previsoesList[4].dia)
        txtTempoCard_05?.text = SiglaDescricao.converterSiglaParaDescricao(previsoesList[4].tempo)
        txtUvCard_05?.text = obterClassificacaoUV(previsoesList[4].iuv)

        val resourceIdTempoCard_05 = resources.getIdentifier(
            previsoesList[4].tempo,
            "drawable",
            requireActivity().packageName
        )
        imgTempoCard_05?.setImageResource(resourceIdTempoCard_05)

        // Card 06
        val txtDiaCard_06 = view?.findViewById<TextView>(R.id.txtDiaCard_06)
        val txtMesCard_06 = view?.findViewById<TextView>(R.id.txtMesCard_06)
        val txtTempoCard_06 = view?.findViewById<TextView>(R.id.txtTempoCard_06)
        val txtUvCard_06 = view?.findViewById<TextView>(R.id.txtUvCard_06)
        val imgTempoCard_06 = view?.findViewById<ImageView>(R.id.imgTempoCard_06)

        txtDiaCard_06?.text = obterDia(previsoesList[5].dia)
        txtMesCard_06?.text = obterMes(previsoesList[5].dia)
        txtTempoCard_06?.text = SiglaDescricao.converterSiglaParaDescricao(previsoesList[5].tempo)
        txtUvCard_06?.text = obterClassificacaoUV(previsoesList[5].iuv)

        val resourceIdTempoCard_06 = resources.getIdentifier(
            previsoesList[5].tempo,
            "drawable",
            requireActivity().packageName
        )
        imgTempoCard_06?.setImageResource(resourceIdTempoCard_06)
    }


    private fun obterDia(data: String?): String {
        if (data == null || data.isEmpty() || data == "null") {
            return "-"
        }
        val localDate = LocalDate.parse(data)
        val dia = localDate.dayOfMonth
        return dia.toString()
    }

    private fun obterMes(data: String?): String {
        if (data == null || data.isEmpty() || data == "null") {
            return "-"
        }
        val localDate = LocalDate.parse(data)
        val formatter = DateTimeFormatter.ofPattern("MMM", Locale("pt", "BR"))
        return localDate.format(formatter)
    }

    private fun obterDiaMes(data: String?): String {
        if (data == null || data.isEmpty() || data == "null") {
            return "-"
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val calendar = Calendar.getInstance()
        return try {
            val date = sdf.parse(data)
            calendar.time = date
            val dia = calendar.get(Calendar.DAY_OF_MONTH)
            val mes = calendar.get(Calendar.MONTH)
            val dfs = DateFormatSymbols(Locale("pt", "BR"))
            val meses = dfs.months
            val mesFormatado = meses[mes]
            String.format("%02d %s", dia, mesFormatado)
        } catch (e: ParseException) {
            e.printStackTrace()
            "-"
        }
    }

    private fun obterDiaDaSemana(data: String?): String {
        if (data == null || data.isEmpty() || data == "null") {
            return "-"
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val calendar = Calendar.getInstance()
        return try {
            val date = sdf.parse(data)
            calendar.time = date
            val dfs = DateFormatSymbols()
            val diasDaSemana = dfs.shortWeekdays
            val diaDaSemana = calendar.get(Calendar.DAY_OF_WEEK)
            diasDaSemana[diaDaSemana].toUpperCase()
        } catch (e: ParseException) {
            e.printStackTrace()
            "-"
        }
    }

    private fun obterClassificacaoUV(indiceUV: String?): String {
        if (indiceUV == null) {
            return "-"
        }
        val indiceUVDouble = indiceUV.toDouble()
        return when {
            indiceUVDouble < 3.0 -> "Baixo ($indiceUV)"
            indiceUVDouble < 6.0 -> "Moderado ($indiceUV)"
            indiceUVDouble < 8.0 -> "Alto ($indiceUV)"
            indiceUVDouble < 11.0 -> "Muito Alto ($indiceUV)"
            indiceUVDouble >= 11.0 -> "Extremo ($indiceUV)"
            else -> "-"
        }
    }

    fun compartilhar(view: View?) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Previsão do tempo")
        val data = previsoesList[indexAtual].dia
        val formatoEntrada = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatoSaida = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        try {
            val dataFormatada = formatoEntrada.parse(data)
            val dataFormatadaString = formatoSaida.format(dataFormatada)
            intent.putExtra(
                Intent.EXTRA_TEXT, "🌤️ Previsão do tempo para: \n" + cidadeNomeUF + "\n\n" +
                        "📅 Data: " + dataFormatadaString + "\n" +
                        "⛅ Tempo: " + SiglaDescricao.converterSiglaParaDescricao(previsoesList[indexAtual].tempo) + "\n" +
                        "🌡️ Temperatura:  " + "Max: " + previsoesList[indexAtual].maxima + "°C" + " - Min: " + previsoesList[indexAtual].minima + "°C\n" +
                        "☀️ Índice UV: " + obterClassificacaoUV(previsoesList[indexAtual].iuv)
            )
            startActivity(Intent.createChooser(intent, "Compartilhar"))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }
}
