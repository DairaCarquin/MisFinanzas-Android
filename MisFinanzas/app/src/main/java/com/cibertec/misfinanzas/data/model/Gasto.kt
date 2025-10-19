package com.cibertec.misfinanzas.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gastos")
data class Gasto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val monto: Double,
    val descripcion: String?,
    val categoria: String,
    val fecha: Long
)
