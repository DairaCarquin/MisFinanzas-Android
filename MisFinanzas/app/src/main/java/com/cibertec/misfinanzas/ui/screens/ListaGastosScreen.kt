package com.cibertec.misfinanzas.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cibertec.misfinanzas.ui.components.GastoItem
import com.cibertec.misfinanzas.viewmodel.GastoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaGastosScreen(
    viewModel: GastoViewModel,
    onAddClick: () -> Unit
) {
    val gastos by viewModel.gastos.collectAsStateWithLifecycle(emptyList())
    val gastosOrdenados = gastos.sortedByDescending { it.fecha }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFF0F1D13)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 14.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                Text(
                    text = "Mis Gastos",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Center)
                )

                IconButton(
                    onClick = onAddClick,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(42.dp)
                        .background(Color(0xFF00C853), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar gasto",
                        tint = Color.White
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(gastosOrdenados) { gasto ->
                    GastoItem(
                        gasto = gasto,
                        onEliminar = {
                            viewModel.eliminar(it)
                            scope.launch {
                                snackbarHostState.showSnackbar("🗑️ Gasto eliminado")
                            }
                        }
                    )
                }

                val total = gastos.sumOf { it.monto }
                item {
                    Text(
                        text = "Total: -$${"%.2f".format(total)}",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp, bottom = 20.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
