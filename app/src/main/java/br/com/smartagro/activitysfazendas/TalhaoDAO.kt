import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class TalhaoDAO(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "smartAgro.db"
        private const val TABLE_NAME = "talhoes"

        private const val COL_ID = "id"
        private const val COL_FAZENDA_ID = "fazendaId"
        private const val COL_CULTURA = "cultura"
        private const val COL_AREA = "area"
        private const val COL_DATA_PLANTIO = "dataPlantio"
        private const val COL_DATA_COLHEITA = "dataColheita"
        private const val COL_ATIVIDADES = "atividades"
        private const val COL_CUSTOS = "custos"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableSQL = "CREATE TABLE $TABLE_NAME (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COL_FAZENDA_ID INTEGER," +
                "$COL_CULTURA TEXT," +
                "$COL_AREA REAL," +
                "$COL_DATA_PLANTIO TEXT," +
                "$COL_DATA_COLHEITA TEXT," +
                "$COL_ATIVIDADES TEXT," +
                "$COL_CUSTOS REAL," +
                "FOREIGN KEY($COL_FAZENDA_ID) REFERENCES fazendas(id)" +
                ")"
        db.execSQL(createTableSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertTalhao(talhao: Talhao): Long {
        val db = this.writableDatabase
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dataPlantioStr = dateFormat.format(talhao.dataPlantio)
        val dataColheitaStr = dateFormat.format(talhao.dataPrevistaColheita)
        val values = ContentValues().apply {
            put(COL_FAZENDA_ID, talhao.fazendaId)
            put(COL_CULTURA, talhao.cultura)
            put(COL_AREA, talhao.area)
            put(COL_DATA_PLANTIO, dataPlantioStr)
            put(COL_DATA_COLHEITA, dataColheitaStr)
            put(COL_ATIVIDADES, talhao.atividades)
            put(COL_CUSTOS, talhao.custo)
        }
        val newRowId = db.insert(TABLE_NAME, null, values)
        db.close()
        return newRowId
    }

    fun updateTalhao(talhao: Talhao): Int {
        val db = this.writableDatabase
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dataPlantioStr = dateFormat.format(talhao.dataPlantio)
        val dataColheitaStr = dateFormat.format(talhao.dataPrevistaColheita)
        val values = ContentValues().apply {
            put(COL_FAZENDA_ID, talhao.fazendaId)
            put(COL_CULTURA, talhao.cultura)
            put(COL_AREA, talhao.area)
            put(COL_DATA_PLANTIO, dataPlantioStr)
            put(COL_DATA_COLHEITA, dataColheitaStr)
            put(COL_ATIVIDADES, talhao.atividades)
            put(COL_CUSTOS, talhao.custo)
        }
        val updatedRows = db.update(TABLE_NAME, values, "$COL_ID = ?", arrayOf(talhao.id.toString()))
        db.close()
        return updatedRows
    }

    fun deleteTalhao(talhaoId: Long): Int {
        val db = this.writableDatabase
        val deletedRows = db.delete(TABLE_NAME, "$COL_ID = ?", arrayOf(talhaoId.toString()))
        db.close()
        return deletedRows
    }

    @SuppressLint("Range")
    fun getAllTalhoes(): List<Talhao> {
        val talhoesList = mutableListOf<Talhao>()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(COL_ID))
                val fazendaId = cursor.getLong(cursor.getColumnIndex(COL_FAZENDA_ID))
                val cultura = cursor.getString(cursor.getColumnIndex(COL_CULTURA))
                val area = cursor.getDouble(cursor.getColumnIndex(COL_AREA))
                val dataPlantio = dateFormat.parse(cursor.getString(cursor.getColumnIndex(COL_DATA_PLANTIO))) ?: Date()
                val dataColheita = dateFormat.parse(cursor.getString(cursor.getColumnIndex(COL_DATA_COLHEITA))) ?: Date()
                val atividades = cursor.getString(cursor.getColumnIndex(COL_ATIVIDADES))
                val custos = cursor.getDouble(cursor.getColumnIndex(COL_CUSTOS))
                talhoesList.add(Talhao(id, fazendaId, cultura, area, dataPlantio, dataColheita, atividades, custos))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return talhoesList
    }
}
