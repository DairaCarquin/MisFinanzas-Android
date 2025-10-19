package com.cibertec.misfinanzas.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.cibertec.misfinanzas.data.model.Gasto
import com.cibertec.misfinanzas.ui.components.CategoriaDropdown
import com.cibertec.misfinanzas.viewmodel.GastoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarGastoScreen(
    viewModel: GastoViewModel,
    onGuardarGasto: (Gasto) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var monto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf(Date()) }

    var showMontoDialog by remember { mutableStateOf(false) }
    var showCategoriaDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val calendar = Calendar.getInstance()

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    containerColor = if (data.visuals.message.contains("⚠️")) Color(0xFFFF9800) else Color(0xFF00C853),
                    contentColor = Color.White,
                    action = {
                        Icon(
                            imageVector = if (data.visuals.message.contains("⚠️"))
                                Icons.Default.Warning else Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                ) {
                    Text(
                        text = data.visuals.message,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        containerColor = Color(0xFF0F1D13)
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Registrar Nuevo Gasto",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = monto,
                onValueChange = { monto = it },
                label = { Text("Monto") },
                leadingIcon = { Text("S/") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00C853),
                    unfocusedBorderColor = Color(0xFF2A2A2A),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color(0xFF00C853),
                    unfocusedLabelColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00C853),
                    unfocusedBorderColor = Color(0xFF2A2A2A),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color(0xFF00C853),
                    unfocusedLabelColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = formatoFecha.format(fecha),
                onValueChange = {},
                label = { Text("Fecha") },
                trailingIcon = {
                    IconButton(onClick = {
                        val datePicker = DatePickerDialog(
                            context,
                            { _, year, month, day ->
                                calendar.set(year, month, day)
                                fecha = calendar.time
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        )
                        datePicker.show()
                    }) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Seleccionar fecha",
                            tint = Color(0xFF00C853)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00C853),
                    unfocusedBorderColor = Color(0xFF2A2A2A),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color(0xFF00C853),
                    unfocusedLabelColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            CategoriaDropdown(
                categoriaSeleccionada = categoria,
                onCategoriaSeleccionada = { categoria = it }
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    val montoDouble = monto.toDoubleOrNull() ?: 0.0

                    when {
                        montoDouble <= 0 -> showMontoDialog = true
                        categoria.isEmpty() -> showCategoriaDialog = true
                        else -> {
                            scope.launch {
                                val totalMesActual = viewModel.totalMensual(categoria)
                                val limite = obtenerLimiteCategoria(categoria)
                                val totalConNuevo = totalMesActual + montoDouble

                                if (totalConNuevo > limite) {
                                    snackbarHostState.showSnackbar(
                                        "⚠️ No puedes registrar este gasto. Has superado el límite de $categoria: S/$totalConNuevo de S/$limite"
                                    )
                                } else {
                                    val nuevoGasto = Gasto(
                                        monto = montoDouble,
                                        descripcion = if (descripcion.isEmpty()) categoria else descripcion,
                                        categoria = categoria,
                                        fecha = fecha.time
                                    )

                                    onGuardarGasto(nuevoGasto)

                                    snackbarHostState.showSnackbar("✅ Gasto guardado correctamente")

                                    delay(1500)

                                    monto = ""
                                    descripcion = ""
                                    categoria = ""
                                    fecha = Date()

                                    onNavigateBack()
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                shape = CircleShape
            ) {
                Text("Guardar Gasto", color = Color.White, fontWeight = FontWeight.Bold)
            }

        }
    }

    if (showMontoDialog) {
        AlertDialog(
            onDismissRequest = { showMontoDialog = false },
            title = { Text("⚠️ Monto inválido") },
            text = { Text("⚠️ Por favor ingresa un monto mayor a 0.") },
            confirmButton = {
                TextButton(onClick = { showMontoDialog = false }) {
                    Text("Aceptar", color = Color(0xFF00C853))
                }
            }
        )
    }

    if (showCategoriaDialog) {
        AlertDialog(
            onDismissRequest = { showCategoriaDialog = false },
            title = { Text("⚠️ Categoría requerida") },
            text = { Text("⚠️ Debes seleccionar una categoría antes de guardar.") },
            confirmButton = {
                TextButton(onClick = { showCategoriaDialog = false }) {
                    Text("Aceptar", color = Color(0xFF00C853))
                }
            }
        )
    }
}

fun obtenerLimiteCategoria(categoria: String): Double {
    return when (categoria) {
        "Alimentación" -> 800.0
        "Transporte" -> 300.0
        "Entretenimiento" -> 200.0
        "Vivienda" -> 1500.0
        "Salud" -> 400.0
        "Café/Bebidas" -> 150.0
        "Compras" -> 500.0
        "Otros" -> 300.0
        else -> 0.0
    }
}
