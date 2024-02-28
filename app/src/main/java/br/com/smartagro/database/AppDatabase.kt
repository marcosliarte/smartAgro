package br.com.smartagro.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.alura.orgs.database.dao.UsuarioDao
import br.com.smartagro.model.Usuario

@Database(entities = [Usuario::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {//TODO: ainda não implementado
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        fun instancia(context: Context) : AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "smart_agro.db"
            ).allowMainThreadQueries()
                .build()
        }
    }
}