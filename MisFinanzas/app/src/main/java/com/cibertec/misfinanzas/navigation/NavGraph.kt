package com.cibertec.misfinanzas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cibertec.misfinanzas.ui.screens.ListaGastosScreen
import com.cibertec.misfinanzas.ui.screens.RegistrarGastoScreen
import com.cibertec.misfinanzas.viewmodel.GastoViewModel

sealed class Screen(val route: String) {
    object ListaGastos : Screen("lista_gastos")
    object RegistrarGasto : Screen("registrar_gasto")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: GastoViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.ListaGastos.route
    ) {
        composable(Screen.ListaGastos.route) {
            ListaGastosScreen(
                viewModel = viewModel,
                onAddClick = { navController.navigate(Screen.RegistrarGasto.route) }
            )
        }

        composable(Screen.RegistrarGasto.route) {
            RegistrarGastoScreen(
                viewModel = viewModel,
                onGuardarGasto = { gasto ->
                    viewModel.insertar(gasto)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

    }
}
