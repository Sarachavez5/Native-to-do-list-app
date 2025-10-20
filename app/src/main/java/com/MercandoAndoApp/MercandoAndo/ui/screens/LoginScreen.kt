package com.MercandoAndoApp.MercandoAndo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.MercandoAndoApp.MercandoAndo.ui.navigation.Rutas
import com.MercandoAndoApp.MercandoAndo.ui.viewmodel.AuthEstado
import com.MercandoAndoApp.MercandoAndo.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var correo by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    
    val estadoAuth by authViewModel.estadoAuth.collectAsState()
    
    // Navegar cuando esté autenticado
    LaunchedEffect(estadoAuth) {
        if (estadoAuth is AuthEstado.Autenticado) {
            navController.navigate(Rutas.Listas.ruta) {
                popUpTo(Rutas.Login.ruta) { inclusive = true }
            }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo con icono de carrito
        Surface(
            modifier = Modifier.size(80.dp),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.primary
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Logo",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Título
        Text(
            text = "Bienvenido",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Campo de correo
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo") },
            placeholder = { Text("correo@ejemplo.com") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Campo de contraseña
        OutlinedTextField(
            value = contraseña,
            onValueChange = { contraseña = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Enlace de olvidaste contraseña
        TextButton(
            onClick = { /* TODO: Implementar recuperación */ },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Botón de iniciar sesión
        Button(
            onClick = {
                authViewModel.limpiarError()
                authViewModel.iniciarSesion(correo, contraseña)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = estadoAuth !is AuthEstado.Cargando
        ) {
            if (estadoAuth is AuthEstado.Cargando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Iniciar sesión")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Botón de crear cuenta
        OutlinedButton(
            onClick = {
                authViewModel.limpiarError()
                navController.navigate(Rutas.Registro.ruta)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Crear cuenta")
        }
        
        // Mostrar errores
        if (estadoAuth is AuthEstado.Error) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = (estadoAuth as AuthEstado.Error).mensaje,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

