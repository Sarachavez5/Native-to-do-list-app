package com.MercandoAndoApp.MercandoAndo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.MercandoAndoApp.MercandoAndo.ui.components.BottomNavigationBar
import com.MercandoAndoApp.MercandoAndo.ui.navigation.Rutas
import com.MercandoAndoApp.MercandoAndo.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val usuarioActual by authViewModel.usuarioActual.collectAsState()
    val modoOscuro by authViewModel.modoOscuro.collectAsState()
    var mostrarDialogoAjustes by remember { mutableStateOf(false) }
    var mostrarDialogoCerrarSesion by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                actions = {
                    IconButton(onClick = { mostrarDialogoAjustes = true }) {
                        Icon(Icons.Default.Settings, "Ajustes")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            // Avatar con iniciales
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                usuarioActual?.let { usuario ->
                    Text(
                        text = "${usuario.nombre.first()}${usuario.apellidos.first()}",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Nombre completo
            usuarioActual?.let { usuario ->
                Text(
                    text = "${usuario.nombre} ${usuario.apellidos}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = usuario.correo,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Botón Editar perfil
            OutlinedButton(
                onClick = { /* TODO: Implementar edición de perfil */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Editar perfil")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Botón Cerrar sesión
            Button(
                onClick = { mostrarDialogoCerrarSesion = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Cerrar sesión")
            }
        }
    }
    
    // Diálogo de ajustes (Modo oscuro)
    if (mostrarDialogoAjustes) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoAjustes = false },
            title = { Text("Ajustes") },
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Modo oscuro",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Cambia el tema de la aplicación",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = modoOscuro,
                        onCheckedChange = { authViewModel.alternarModoOscuro() }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { mostrarDialogoAjustes = false }) {
                    Text("CERRAR")
                }
            }
        )
    }
    
    // Diálogo de confirmación de cerrar sesión
    if (mostrarDialogoCerrarSesion) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoCerrarSesion = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Estás seguro de que deseas cerrar sesión?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        authViewModel.cerrarSesion()
                        mostrarDialogoCerrarSesion = false
                        navController.navigate(Rutas.Login.ruta) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                ) {
                    Text("CERRAR SESIÓN")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoCerrarSesion = false }) {
                    Text("CANCELAR")
                }
            }
        )
    }
}

