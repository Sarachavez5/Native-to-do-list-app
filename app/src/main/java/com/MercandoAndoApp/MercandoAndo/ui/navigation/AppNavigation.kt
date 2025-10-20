package com.MercandoAndoApp.MercandoAndo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.MercandoAndoApp.MercandoAndo.ui.viewmodel.AuthEstado
import com.MercandoAndoApp.MercandoAndo.ui.viewmodel.AuthViewModel
import com.MercandoAndoApp.MercandoAndo.ui.screens.*

/**
 * Rutas de navegación de la aplicación
 */
sealed class Rutas(val ruta: String) {
    data object Login : Rutas("login")
    data object Registro : Rutas("registro")
    data object Listas : Rutas("listas")
    data object DetalleLista : Rutas("detalle_lista/{listaId}") {
        fun crearRuta(listaId: Long) = "detalle_lista/$listaId"
    }
    data object AgregarProductos : Rutas("agregar_productos/{listaId}") {
        fun crearRuta(listaId: Long) = "agregar_productos/$listaId"
    }
    data object Perfil : Rutas("perfil")
    data object Creditos : Rutas("creditos")
    data object Papelera : Rutas("papelera")
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val estadoAuth by authViewModel.estadoAuth.collectAsState()
    
    val rutaInicial = when (estadoAuth) {
        is AuthEstado.Autenticado -> Rutas.Listas.ruta
        else -> Rutas.Login.ruta
    }
    
    NavHost(
        navController = navController,
        startDestination = rutaInicial
    ) {
        // Pantallas de autenticación
        composable(Rutas.Login.ruta) {
            LoginScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }
        
        composable(Rutas.Registro.ruta) {
            RegistroScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }
        
        // Pantallas principales
        composable(Rutas.Listas.ruta) {
            ListasScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }
        
        composable(
            route = Rutas.DetalleLista.ruta,
            arguments = listOf(
                navArgument("listaId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val listaId = backStackEntry.arguments?.getLong("listaId") ?: return@composable
            DetalleListaScreen(
                navController = navController,
                listaId = listaId,
                authViewModel = authViewModel
            )
        }
        
        composable(
            route = Rutas.AgregarProductos.ruta,
            arguments = listOf(
                navArgument("listaId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val listaId = backStackEntry.arguments?.getLong("listaId") ?: return@composable
            AgregarProductosScreen(
                navController = navController,
                listaId = listaId,
                authViewModel = authViewModel
            )
        }
        
        composable(Rutas.Perfil.ruta) {
            PerfilScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }
        
        composable(Rutas.Creditos.ruta) {
            CreditosScreen(
                navController = navController
            )
        }
        
        composable(Rutas.Papelera.ruta) {
            PapeleraScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }
    }
}

