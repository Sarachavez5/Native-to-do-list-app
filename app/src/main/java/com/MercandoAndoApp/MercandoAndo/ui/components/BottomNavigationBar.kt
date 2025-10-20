package com.MercandoAndoApp.MercandoAndo.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.MercandoAndoApp.MercandoAndo.ui.navigation.Rutas

/**
 * Barra de navegación inferior con 3 pestañas: Listas, Perfil, Créditos
 */
@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        NavigationBarItem(
            icon = { 
                Icon(
                    if (currentRoute == Rutas.Listas.ruta) Icons.Filled.Assignment else Icons.Outlined.Assignment,
                    "Listas"
                ) 
            },
            label = { Text("Listas") },
            selected = currentRoute == Rutas.Listas.ruta,
            onClick = {
                if (currentRoute != Rutas.Listas.ruta) {
                    navController.navigate(Rutas.Listas.ruta) {
                        popUpTo(Rutas.Listas.ruta) { inclusive = true }
                    }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        
        NavigationBarItem(
            icon = { 
                Icon(
                    if (currentRoute == Rutas.Perfil.ruta) Icons.Filled.Person else Icons.Outlined.Person,
                    "Perfil"
                ) 
            },
            label = { Text("Perfil") },
            selected = currentRoute == Rutas.Perfil.ruta,
            onClick = {
                if (currentRoute != Rutas.Perfil.ruta) {
                    navController.navigate(Rutas.Perfil.ruta)
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        
        NavigationBarItem(
            icon = { 
                Icon(
                    if (currentRoute == Rutas.Creditos.ruta) Icons.Filled.People else Icons.Outlined.People,
                    "Créditos"
                ) 
            },
            label = { Text("Créditos") },
            selected = currentRoute == Rutas.Creditos.ruta,
            onClick = {
                if (currentRoute != Rutas.Creditos.ruta) {
                    navController.navigate(Rutas.Creditos.ruta)
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

