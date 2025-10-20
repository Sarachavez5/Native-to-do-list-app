package com.MercandoAndoApp.MercandoAndo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.MercandoAndoApp.MercandoAndo.data.model.ListaMercado
import com.MercandoAndoApp.MercandoAndo.ui.viewmodel.AuthViewModel
import com.MercandoAndoApp.MercandoAndo.ui.viewmodel.ListasViewModel
import com.MercandoAndoApp.MercandoAndo.ui.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PapeleraScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val usuarioActual by authViewModel.usuarioActual.collectAsState()
    
    val listasViewModel: ListasViewModel = viewModel(
        factory = ViewModelFactory(context, usuarioActual?.id)
    )
    
    val listasEliminadas by listasViewModel.listasEliminadas.collectAsState()
    var mostrarDialogoVaciar by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Papelera") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                actions = {
                    if (listasEliminadas.isNotEmpty()) {
                        TextButton(onClick = { mostrarDialogoVaciar = true }) {
                            Text("Vaciar")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (listasEliminadas.isEmpty()) {
            // Estado vacío
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(120.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No hay listas eliminadas",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(listasEliminadas, key = { it.id }) { lista ->
                    TarjetaListaEliminada(
                        lista = lista,
                        onRestaurar = {
                            listasViewModel.restaurarLista(lista.id)
                        },
                        onEliminar = {
                            listasViewModel.eliminarListaPermanentemente(lista.id)
                        }
                    )
                }
            }
        }
    }
    
    // Diálogo de confirmación para vaciar papelera
    if (mostrarDialogoVaciar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoVaciar = false },
            icon = { Icon(Icons.Default.Delete, null) },
            title = { Text("¿Vaciar toda la papelera?") },
            text = { Text("Esta acción no se puede deshacer. Todas las listas eliminadas serán borradas permanentemente.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        listasViewModel.vaciarPapelera()
                        mostrarDialogoVaciar = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("VACIAR")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoVaciar = false }) {
                    Text("CANCELAR")
                }
            }
        )
    }
}

@Composable
fun TarjetaListaEliminada(
    lista: ListaMercado,
    onRestaurar: () -> Unit,
    onEliminar: () -> Unit
) {
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lista.nombre,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            Row {
                IconButton(onClick = onRestaurar) {
                    Icon(
                        Icons.Default.Refresh,
                        "Restaurar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = { mostrarDialogoEliminar = true }) {
                    Icon(
                        Icons.Default.Delete,
                        "Eliminar permanentemente",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
    
    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            icon = { Icon(Icons.Default.Warning, null, tint = MaterialTheme.colorScheme.error) },
            title = { Text("¿Eliminar permanentemente?") },
            text = { Text("Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onEliminar()
                        mostrarDialogoEliminar = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("ELIMINAR")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminar = false }) {
                    Text("CANCELAR")
                }
            }
        )
    }
}

