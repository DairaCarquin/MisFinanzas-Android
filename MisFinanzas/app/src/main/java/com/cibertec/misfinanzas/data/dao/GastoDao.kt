package com.cibertec.misfinanzas.data.dao

import androidx.room.*
import com.cibertec.misfinanzas.data.model.Gasto
import kotlinx.coroutines.flow.Flow

@Dao
interface GastoDao {

    @Query("SELECT * FROM gastos ORDER BY fecha DESC")
    fun obtenerGastos(): Flow<List<Gasto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(gasto: Gasto)

    @Delete
    suspend fun eliminar(gasto: Gasto)

    @Query("""
    SELECT IFNULL(SUM(monto), 0) 
    FROM gastos 
    WHERE categoria = :categoria 
      AND strftime('%m', datetime(fecha / 1000, 'unixepoch')) = strftime('%m', 'now')
      AND strftime('%Y', datetime(fecha / 1000, 'unixepoch')) = strftime('%Y', 'now')
    """)
    suspend fun totalMensualPorCategoria(categoria: String): Double
}
