package com.MercandoAndoApp.MercandoAndo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.MercandoAndoApp.MercandoAndo.ui.navigation.AppNavigation
import com.MercandoAndoApp.MercandoAndo.ui.theme.AppMercadoTheme
import com.MercandoAndoApp.MercandoAndo.ui.viewmodel.AuthViewModel
import com.MercandoAndoApp.MercandoAndo.ui.viewmodel.ViewModelFactory

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
