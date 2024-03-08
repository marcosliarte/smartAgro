import java.io.Serializable
import java.util.*

data class Talhao(
    var id: Long = 0,
    val fazendaId: Long,
    val cultura: String,
    val area: Double,
    val dataPlantio: Date,
    val dataPrevistaColheita: Date,
    val atividades: String,
    val custo: Double
) : Serializable
