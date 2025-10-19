package com.cibertec.misfinanzas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cibertec.misfinanzas.data.db.AppDatabase
import com.cibertec.misfinanzas.data.model.Gasto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GastoViewModel(application: Application) : AndroidViewModel(application) {

    private val gastoDao = AppDatabase.getDatabase(application).gastoDao()

    val gastos: Flow<List<Gasto>> = gastoDao.obtenerGastos()

    fun insertar(gasto: Gasto) = viewModelScope.launch {
        gastoDao.insertar(gasto)
    }

    fun eliminar(gasto: Gasto) = viewModelScope.launch {
        gastoDao.eliminar(gasto)
    }

    suspend fun totalMensual(categoria: String): Double {
        return gastoDao.totalMensualPorCategoria(categoria)
    }

}
