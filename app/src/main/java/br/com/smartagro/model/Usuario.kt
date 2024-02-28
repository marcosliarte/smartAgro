package br.com.smartagro.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity
@Parcelize
data class Usuario(//TODO: ainda não implementado
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nome: String?,
) : Parcelable
