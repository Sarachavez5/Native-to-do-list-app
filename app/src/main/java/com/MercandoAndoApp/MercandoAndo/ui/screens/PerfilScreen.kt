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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// Importaciones específicas para text input
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.MercandoAndoApp.MercandoAndo.ui.components.BottomNavigationBar
import com.MercandoAndoApp.MercandoAndo.ui.navigation.Rutas
import com.MercandoAndoApp.MercandoAndo.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    // Observar estados
    val usuarioActual by authViewModel.usuarioActual.collectAsState()
    val modoOscuro by authViewModel.modoOscuro.collectAsState()
    val perfilState by authViewModel.perfilUpdateState.collectAsState()
    
    // Estados locales
    var mostrarDialogoAjustes by remember { mutableStateOf(false) }
    var mostrarDialogoCerrarSesion by remember { mutableStateOf(false) }
    var mostrarDialogoEditar by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Estados para la confirmación de éxito (fuera del diálogo de edición)
    var mostrarConfirmacionExito by remember { mutableStateOf(false) }
    var mensajeExito by remember { mutableStateOf("") }

    // Efecto para verificar si el usuario está autenticado
    LaunchedEffect(usuarioActual) {
        if (usuarioActual == null) {
            navController.navigate(Rutas.Login.ruta) {
                popUpTo(0) { inclusive = true }
            }
            return@LaunchedEffect
        }
    }

    // Observador global de estado de actualización de perfil
    LaunchedEffect(perfilState) {
        when (perfilState) {
            is com.MercandoAndoApp.MercandoAndo.ui.viewmodel.PerfilUpdateState.Success -> {
                val msg = (perfilState as com.MercandoAndoApp.MercandoAndo.ui.viewmodel.PerfilUpdateState.Success).mensaje
                mensajeExito = msg
                mostrarDialogoEditar = false
                mostrarConfirmacionExito = true
                // limpiar estado para evitar re-efectos al reabrir el diálogo
                authViewModel.resetPerfilState()
            }
            is com.MercandoAndoApp.MercandoAndo.ui.viewmodel.PerfilUpdateState.Error -> {
                val msg = (perfilState as com.MercandoAndoApp.MercandoAndo.ui.viewmodel.PerfilUpdateState.Error).mensaje
                snackbarHostState.showSnackbar(msg)
                // limpiar el estado de error luego de mostrar
                authViewModel.resetPerfilState()
            }
            else -> { /* Idle o Loading gestionado en UI */ }
        }
    }

    // Diálogo modal de confirmación tras guardar correctamente (fuera del diálogo de edición)
    if (mostrarConfirmacionExito) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacionExito = false },
            title = { Text("Perfil actualizado") },
            text = { Text(mensajeExito.ifBlank { "Los cambios se guardaron correctamente." }) },
            confirmButton = {
                TextButton(onClick = { mostrarConfirmacionExito = false }) {
                    Text("OK")
                }
            }
        )
    }
    
    // Combine los dos Scaffolds en uno
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
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
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
                onClick = {
                    // Limpiar cualquier estado anterior de actualización de perfil
                    authViewModel.resetPerfilState()
                    mostrarDialogoEditar = true
                },
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

    // Diálogo de edición de perfil
    if (mostrarDialogoEditar) {
        val focusManager = LocalFocusManager.current

        // Estados locales del formulario
        var nombre by remember { mutableStateOf(usuarioActual?.nombre ?: "") }
        var apellidos by remember { mutableStateOf(usuarioActual?.apellidos ?: "") }
        var correo by remember { mutableStateOf(usuarioActual?.correo ?: "") }

        var cambiarContrasena by remember { mutableStateOf(false) }
        var contrasenaActual by remember { mutableStateOf("") }
        var nuevaContrasena by remember { mutableStateOf("") }
        var confirmarContrasena by remember { mutableStateOf("") }

        var mostrarContrasenaActual by remember { mutableStateOf(false) }
        var mostrarNuevaContrasena by remember { mutableStateOf(false) }
        var mostrarConfirmarContrasena by remember { mutableStateOf(false) }

        var nombreError by remember { mutableStateOf<String?>(null) }
        var apellidosError by remember { mutableStateOf<String?>(null) }
        var correoError by remember { mutableStateOf<String?>(null) }
        var contrasenaError by remember { mutableStateOf<String?>(null) }

        val cargando = perfilState is com.MercandoAndoApp.MercandoAndo.ui.viewmodel.PerfilUpdateState.Loading

        LaunchedEffect(Unit) { /* placeholder inside dialog; actual perfilState handling está fuera */ }

        AlertDialog(
            onDismissRequest = { if (!cargando) mostrarDialogoEditar = false },
            title = { Text("Editar perfil") },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = {
                            nombre = it
                            nombreError = null
                        },
                        label = { Text("Nombre") },
                        isError = nombreError != null,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (nombreError != null) Text(nombreError!!, color = MaterialTheme.colorScheme.error)

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = apellidos,
                        onValueChange = {
                            apellidos = it
                            apellidosError = null
                        },
                        label = { Text("Apellidos") },
                        isError = apellidosError != null,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (apellidosError != null) Text(apellidosError!!, color = MaterialTheme.colorScheme.error)

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = correo,
                        onValueChange = {
                            correo = it
                            correoError = null
                        },
                        label = { Text("Correo") },
                        isError = correoError != null,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (correoError != null) Text(correoError!!, color = MaterialTheme.colorScheme.error)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = cambiarContrasena, onCheckedChange = { cambiarContrasena = it })
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cambiar contraseña")
                    }

                    if (cambiarContrasena) {
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = contrasenaActual,
                            onValueChange = { contrasenaActual = it; contrasenaError = null },
                            label = { Text("Contraseña actual") },
                            singleLine = true,
                            visualTransformation = if (mostrarContrasenaActual) 
                                VisualTransformation.None 
                            else 
                                PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Next
                            ),
                            trailingIcon = {
                                TextButton(onClick = { mostrarContrasenaActual = !mostrarContrasenaActual }) {
                                    Text(if (mostrarContrasenaActual) "OCULTAR" else "MOSTRAR")
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = nuevaContrasena,
                            onValueChange = { nuevaContrasena = it; contrasenaError = null },
                            label = { Text("Nueva contraseña") },
                            singleLine = true,
                            visualTransformation = if (mostrarNuevaContrasena) 
                                VisualTransformation.None 
                            else 
                                PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Next
                            ),
                            trailingIcon = {
                                TextButton(onClick = { mostrarNuevaContrasena = !mostrarNuevaContrasena }) {
                                    Text(if (mostrarNuevaContrasena) "OCULTAR" else "MOSTRAR")
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = confirmarContrasena,
                            onValueChange = { confirmarContrasena = it; contrasenaError = null },
                            label = { Text("Confirmar nueva contraseña") },
                            singleLine = true,
                            visualTransformation = if (mostrarConfirmarContrasena) 
                                VisualTransformation.None 
                            else 
                                PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            trailingIcon = {
                                TextButton(onClick = { mostrarConfirmarContrasena = !mostrarConfirmarContrasena }) {
                                    Text(if (mostrarConfirmarContrasena) "OCULTAR" else "MOSTRAR")
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (contrasenaError != null) Text(contrasenaError!!, color = MaterialTheme.colorScheme.error)
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Validaciones front-end rápidas
                        var anyError = false
                        if (nombre.isBlank()) { nombreError = "El nombre no puede estar vacío"; anyError = true }
                        if (apellidos.isBlank()) { apellidosError = "Los apellidos no pueden estar vacíos"; anyError = true }
                        if (correo.isBlank() || !correo.contains("@")) { correoError = "Correo inválido"; anyError = true }

                        if (cambiarContrasena) {
                            if (contrasenaActual.isBlank()) { contrasenaError = "Ingresa la contraseña actual"; anyError = true }
                            if (nuevaContrasena.length < 6) { contrasenaError = "La nueva contraseña debe tener al menos 6 caracteres"; anyError = true }
                            if (nuevaContrasena != confirmarContrasena) { contrasenaError = "Las contraseñas no coinciden"; anyError = true }
                        }

                        if (!anyError) {
                            focusManager.clearFocus(true)
                            authViewModel.actualizarPerfil(
                                nombre = nombre,
                                apellidos = apellidos,
                                correo = correo,
                                cambiarContrasena = cambiarContrasena,
                                contrasenaActual = contrasenaActual.takeIf { it.isNotBlank() },
                                nuevaContrasena = nuevaContrasena.takeIf { it.isNotBlank() },
                                confirmarContrasena = confirmarContrasena.takeIf { it.isNotBlank() }
                            )
                        }
                    },
                    enabled = !cargando
                ) {
                    if (cargando) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Text("GUARDAR")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { if (!cargando) mostrarDialogoEditar = false }) {
                    Text("CANCELAR")
                }
            }
        )
    }
}

