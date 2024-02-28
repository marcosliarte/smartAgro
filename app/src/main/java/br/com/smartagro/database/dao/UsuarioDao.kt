package br.com.alura.orgs.database.dao

import androidx.room.*
import br.com.smartagro.model.Usuario

@Dao
interface UsuarioDao {//TODO: ainda não implementado

    @Query("SELECT * FROM Usuario")
    fun buscaTodos() : List<Usuario>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun salvar(vararg produto: Usuario)

    @Delete
    fun remove(produto: Usuario)

    @Query("SELECT * FROM Usuario WHERE id = :id")
    fun buscaPorId(id: Long) : Usuario?

}