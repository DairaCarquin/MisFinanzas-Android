package com.cibertec.misfinanzas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.cibertec.misfinanzas.navigation.NavGraph
import com.cibertec.misfinanzas.ui.theme.MisFinanzasTheme
import com.cibertec.misfinanzas.viewmodel.GastoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MisFinanzasTheme {
                val navController = rememberNavController()
                val viewModel: GastoViewModel = viewModel()

                NavGraph(navController = navController, viewModel = viewModel)
            }
        }
    }
}
