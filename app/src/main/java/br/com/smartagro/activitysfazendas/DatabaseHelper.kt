import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "smartAgro.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS fazendas (" +
                    "id INTEGER PRIMARY KEY," +
                    "nome TEXT," +
                    "tamanho REAL," +
                    "localizacao TEXT" +
                    ")"
        )
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS talhoes (" +
                    "id INTEGER PRIMARY KEY," +
                    "fazendaId INTEGER," +
                    "cultura TEXT," +
                    "area REAL," +
                    "dataPlantio TEXT," +
                    "dataColheita TEXT," +
                    "atividades TEXT," +
                    "custos REAL," +
                    "FOREIGN KEY(fazendaId) REFERENCES fazendas(id)" +
                    ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS fazendas")
        db.execSQL("DROP TABLE IF EXISTS talhoes")
        onCreate(db)
    }

    fun inserirTalhao(talhao: Talhao): Long {
        val db = this.writableDatabase

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dataPlantioStr = dateFormat.format(talhao.dataPlantio)
        val dataColheitaStr = dateFormat.format(talhao.dataPrevistaColheita)

        val values = ContentValues().apply {
            put("fazendaId", talhao.fazendaId)
            put("cultura", talhao.cultura)
            put("area", talhao.area)
            put("dataPlantio", dataPlantioStr)
            put("dataColheita", dataColheitaStr)
            put("atividades", talhao.atividades)
            put("custos", talhao.custo)
        }

        val id = db.insert("talhoes", null, values)
        db.close()
        return id
    }


    fun atualizarTalhao(talhao: Talhao): Int {
        val db = this.writableDatabase
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dataPlantioStr = dateFormat.format(talhao.dataPlantio)
        val dataColheitaStr = dateFormat.format(talhao.dataPrevistaColheita)

        val values = ContentValues().apply {
            put("cultura", talhao.cultura)
            put("area", talhao.area)
            put("dataPlantio", dataPlantioStr) // Tratando a data como String
            put("dataColheita", dataColheitaStr) // Tratando a data como String
            put("atividades", talhao.atividades)
            put("custos", talhao.custo)
        }

        val success = db.update("talhoes", values, "id=?", arrayOf(talhao.id.toString()))
        db.close()
        return success
    }



    fun deletarTalhao(talhaoId: Int): Int {
        val db = this.writableDatabase
        val success = db.delete("talhoes", "id=?", arrayOf(talhaoId.toString()))
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun buscarTalhoesPorFazenda(fazendaId: Long): List<Talhao> {
        val talhoesList = mutableListOf<Talhao>()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM talhoes WHERE fazendaId = ?"
        val cursor = db.rawQuery(selectQuery, arrayOf(fazendaId.toString()))
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex("id"))
                val cultura = cursor.getString(cursor.getColumnIndex("cultura"))
                val area = cursor.getDouble(cursor.getColumnIndex("area"))
                val atividades = cursor.getString(cursor.getColumnIndex("atividades"))
                val custos = cursor.getDouble(cursor.getColumnIndex("custos"))
                val dataPlantioStr = cursor.getString(cursor.getColumnIndex("dataPlantio"))
                val dataColheitaStr = cursor.getString(cursor.getColumnIndex("dataColheita"))
                val dataPlantio = dateFormat.parse(dataPlantioStr)
                val dataColheita = dateFormat.parse(dataColheitaStr)

                // Verifica se dataPlantio e dataColheita não são nulas antes de adicionar à lista
                if (dataPlantio != null && dataColheita != null) {
                    val talhao = Talhao(id, fazendaId, cultura, area, dataPlantio, dataColheita, atividades, custos)
                    talhoesList.add(talhao)
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return talhoesList
    }


}

