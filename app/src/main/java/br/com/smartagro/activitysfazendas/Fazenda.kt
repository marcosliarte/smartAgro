import java.io.Serializable

data class Fazenda(
    var id: Long = 0,
    val nome: String,
    val tamanho: Double,
    val localizacao: String
) : Serializable
