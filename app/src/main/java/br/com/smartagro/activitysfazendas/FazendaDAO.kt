import android.content.ContentValues
import android.content.Context
import android.util.Log

class FazendaDAO(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun inserirFazenda(nome: String, tamanho: Double, localizacao: String): Long {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("nome", nome) // nome é uma String
            put("tamanho", tamanho) // tamanho é um Double
            put("localizacao", localizacao) // localizacao é uma String
        }

        val novoId = db.insert("fazendas", null, values)
        db.close()
        Log.d("FazendaDAO", "Fazenda inserida com sucesso. ID: $novoId")
        return novoId
    }

    fun buscarFazendas(): List<Fazenda> {
        val db = dbHelper.readableDatabase
        val projection = arrayOf("id", "nome", "tamanho", "localizacao")

        val cursor = db.query(
            "fazendas",
            projection,
            null,
            null,
            null,
            null,
            null
        )

        val fazendas = mutableListOf<Fazenda>()
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow("id"))
                val nome = getString(getColumnIndexOrThrow("nome"))
                val tamanho = getDouble(getColumnIndexOrThrow("tamanho"))
                val localizacao = getString(getColumnIndexOrThrow("localizacao"))
                fazendas.add(Fazenda(id, nome, tamanho, localizacao))
            }
        }
        cursor.close()
        db.close()
        Log.d("FazendaDAO", "Fazendas recuperadas com sucesso: $fazendas")
        return fazendas
    }

    fun atualizarFazenda(id: Long, nome: String, tamanho: Double, localizacao: String): Int {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("nome", nome)
            put("tamanho", tamanho)
            put("localizacao", localizacao)
        }

        // O 'id' é um Long, que representa o ID da fazenda a ser atualizada
        val count = db.update(
            "fazendas",
            values,
            "id = ?",
            arrayOf(id.toString())
        )
        db.close()
        Log.d("FazendaDAO", "Fazenda atualizada com sucesso. ID: $id")
        return count
    }

    fun excluirFazenda(id: Long): Int {
        val db = dbHelper.writableDatabase

        val count = db.delete(
            "fazendas",
            "id = ?",
            arrayOf(id.toString())
        )
        db.close()
        Log.d("FazendaDAO", "Fazenda excluída com sucesso. ID: $id")
        return count
    }
}
