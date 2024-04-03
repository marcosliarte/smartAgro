package br.com.smartagro.activitysfazendas

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "smartAgro.db"
        const val TABLE_ADUBACOES = "adubacoes"
        const val COLUMN_ID = "id"
        const val COLUMN_DATA_ADUBACAO = "data_adubacao"
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
        // Criação da tabela para armazenar os dados de adubação
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS $TABLE_ADUBACOES (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY," +
                    "$COLUMN_DATA_ADUBACAO TEXT" +
                    ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS fazendas")
        db.execSQL("DROP TABLE IF EXISTS talhoes")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ADUBACOES")
        onCreate(db)
    }

    fun addAdubacao(dataAdubacao: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_DATA_ADUBACAO, dataAdubacao)
        }

        val db = this.writableDatabase
        val id = db.insert(TABLE_ADUBACOES, null, values)
        db.close()
        return id
    }

    fun getAdubacoes(): List<String> {
        val adubacoes = mutableListOf<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_ADUBACOES", null)

        if (cursor.moveToFirst()) {
            do {
                adubacoes.add(cursor.getString(cursor.getColumnIndex(COLUMN_DATA_ADUBACAO)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return adubacoes
    }

    fun deleteAdubacao(dataAdubacao: String): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_ADUBACOES, "$COLUMN_DATA_ADUBACAO = ?", arrayOf(dataAdubacao))
        db.close()
        return result
    }
}
