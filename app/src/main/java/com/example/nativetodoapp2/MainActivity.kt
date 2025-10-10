package com.example.nativetodoapp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.nativetodoapp2.ui.navigation.AppNavigation
import com.example.nativetodoapp2.ui.theme.AppMercadoTheme
import com.example.nativetodoapp2.ui.viewmodel.AuthViewModel
import com.example.nativetodoapp2.ui.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // ViewModel de autenticación
            val authViewModel: AuthViewModel = viewModel(
                factory = ViewModelFactory(applicationContext)
            )
            
            // Estado del modo oscuro
            val modoOscuro by authViewModel.modoOscuro.collectAsState()
            
            // Tema de la aplicación
            AppMercadoTheme(modoOscuro = modoOscuro) {
                val navController = rememberNavController()
                
                AppNavigation(
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
        }
    }
}
