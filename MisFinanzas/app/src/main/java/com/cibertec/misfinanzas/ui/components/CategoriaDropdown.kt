package com.cibertec.misfinanzas.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CategoriaDropdown(
    categoriaSeleccionada: String,
    onCategoriaSeleccionada: (String) -> Unit
) {
    val categorias = listOf(
        Triple("Alimentación", Icons.Default.Fastfood, 800.0),
        Triple("Transporte", Icons.Default.DirectionsBus, 300.0),
        Triple("Entretenimiento", Icons.Default.LocalMovies, 200.0),
        Triple("Vivienda", Icons.Default.Home, 1500.0),
        Triple("Salud", Icons.Default.LocalPharmacy, 400.0),
        Triple("Café/Bebidas", Icons.Default.Coffee, 150.0),
        Triple("Compras", Icons.Default.LocalMall, 500.0),
        Triple("Otros", Icons.Default.LocalShipping, 300.0)
    )

    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color(0xFF142018),
                contentColor = Color.White
            )
        ) {
            Text(
                text = if (categoriaSeleccionada.isEmpty()) "Seleccionar categoría" else categoriaSeleccionada,
                modifier = Modifier.weight(1f)
            )
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color(0xFF1B2A21)
        ) {
            categorias.forEach { (nombre, icono, limite) ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = icono,
                                    contentDescription = nombre,
                                    tint = Color(0xFF00C853),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(nombre, color = Color.White)
                            }
                            Text("Límite: S/$limite", color = Color.Gray, fontSize = MaterialTheme.typography.bodySmall.fontSize)
                        }
                    },
                    onClick = {
                        onCategoriaSeleccionada(nombre)
                        expanded = false
                    }
                )
            }
        }
    }
}
