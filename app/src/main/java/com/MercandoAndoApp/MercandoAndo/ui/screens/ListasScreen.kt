package com.MercandoAndoApp.MercandoAndo.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.MercandoAndoApp.MercandoAndo.data.model.ListaConItems
import com.MercandoAndoApp.MercandoAndo.ui.components.BottomNavigationBar
import com.MercandoAndoApp.MercandoAndo.ui.navigation.Rutas
import com.MercandoAndoApp.MercandoAndo.ui.viewmodel.AuthViewModel
import com.MercandoAndoApp.MercandoAndo.ui.viewmodel.ListasViewModel
import com.MercandoAndoApp.MercandoAndo.ui.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListasScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val usuarioActual by authViewModel.usuarioActual.collectAsState()
    
    val listasViewModel: ListasViewModel = viewModel(
        factory = ViewModelFactory(context, usuarioActual?.id)
    )
    
    val listas by listasViewModel.listasActivas.collectAsState()
    var mostrarDialogoNuevaLista by remember { mutableStateOf(false) }
    var mostrarMenuOpciones by remember { mutableStateOf(false) }
    var listaSeleccionada by remember { mutableStateOf<ListaConItems?>(null) }
    
    // Mostrar mensajes toast
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        listasViewModel.mensajeToast.collect { mensaje ->
            snackbarHostState.showSnackbar(mensaje)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis listas") },
                actions = {
                    IconButton(onClick = { mostrarMenuOpciones = true }) {
                        Icon(Icons.Default.MoreVert, "Más opciones")
                    }
                    
                    DropdownMenu(
                        expanded = mostrarMenuOpciones,
                        onDismissRequest = { mostrarMenuOpciones = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Ver papelera") },
                            onClick = {
                                mostrarMenuOpciones = false
                                navController.navigate(Rutas.Papelera.ruta)
                            },
                            leadingIcon = { Icon(Icons.Default.Delete, null) }
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarDialogoNuevaLista = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, "Nueva lista")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (listas.isEmpty()) {
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
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(120.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No tienes listas",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Crea tu primera lista de mercado",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            // Mostrar listas
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(listas, key = { it.lista.id }) { listaConItems ->
                    TarjetaLista(
                        listaConItems = listaConItems,
                        onClick = {
                            navController.navigate(Rutas.DetalleLista.crearRuta(listaConItems.lista.id))
                        },
                        onMenuClick = {
                            listaSeleccionada = listaConItems
                        }
                    )
                }
            }
        }
    }
    
    // Diálogo para crear nueva lista
    if (mostrarDialogoNuevaLista) {
        var nombreLista by remember { mutableStateOf("") }
        
        AlertDialog(
            onDismissRequest = { mostrarDialogoNuevaLista = false },
            title = { Text("Crear una nueva lista") },
            text = {
                OutlinedTextField(
                    value = nombreLista,
                    onValueChange = { nombreLista = it },
                    label = { Text("Nombre de la lista") },
                    placeholder = { Text("Nueva lista") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        listasViewModel.crearLista(nombreLista)
                        mostrarDialogoNuevaLista = false
                        nombreLista = ""
                    }
                ) {
                    Text("GUARDAR")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoNuevaLista = false }) {
                    Text("CANCELAR")
                }
            }
        )
    }
    
    // Diálogo de opciones de lista
    listaSeleccionada?.let { lista ->
        var mostrarOpcionesCopia by remember { mutableStateOf(false) }
        
        AlertDialog(
            onDismissRequest = { listaSeleccionada = null },
            title = { Text(lista.lista.nombre) },
            text = {
                Column {
                    ListItem(
                        headlineContent = { Text("Renombrar") },
                        leadingContent = { Icon(Icons.Default.Edit, null) },
                        modifier = Modifier.clickable {
                            // TODO: Implementar diálogo de renombrar
                            listaSeleccionada = null
                        }
                    )
                    ListItem(
                        headlineContent = { Text("Copiar") },
                        leadingContent = { Icon(Icons.Default.Star, null) },
                        modifier = Modifier.clickable {
                            mostrarOpcionesCopia = true
                        }
                    )
                    ListItem(
                        headlineContent = { Text("Borrar") },
                        leadingContent = { Icon(Icons.Default.Delete, null) },
                        modifier = Modifier.clickable {
                            listasViewModel.eliminarLista(lista.lista.id)
                            listaSeleccionada = null
                        }
                    )
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { listaSeleccionada = null }) {
                    Text("CERRAR")
                }
            }
        )
        
        if (mostrarOpcionesCopia) {
            AlertDialog(
                onDismissRequest = { mostrarOpcionesCopia = false },
                title = { Text("¿Qué deseas copiar?") },
                text = {
                    Column {
                        ListItem(
                            headlineContent = { Text("Lista completa") },
                            modifier = Modifier.clickable {
                                listasViewModel.copiarListaCompleta(lista.lista.id, lista.lista.nombre)
                                mostrarOpcionesCopia = false
                                listaSeleccionada = null
                            }
                        )
                        ListItem(
                            headlineContent = { Text("Artículos no comprados") },
                            modifier = Modifier.clickable {
                                listasViewModel.copiarSoloNoComprados(lista.lista.id, lista.lista.nombre)
                                mostrarOpcionesCopia = false
                                listaSeleccionada = null
                            }
                        )
                        ListItem(
                            headlineContent = { Text("Artículos comprados") },
                            modifier = Modifier.clickable {
                                listasViewModel.copiarSoloComprados(lista.lista.id, lista.lista.nombre)
                                mostrarOpcionesCopia = false
                                listaSeleccionada = null
                            }
                        )
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = { mostrarOpcionesCopia = false }) {
                        Text("CANCELAR")
                    }
                }
            )
        }
    }
}

@Composable
fun TarjetaLista(
    listaConItems: ListaConItems,
    onClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    val progreso = listaConItems.progresoPercentual() / 100f
    val progresoAnimado by animateFloatAsState(targetValue = progreso, label = "progreso")
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = listaConItems.lista.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onMenuClick) {
                    Icon(Icons.Default.MoreVert, "Opciones")
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Barra de progreso con color verde según wireframe
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(Color(0xFFE5E7EB)) // Gris claro del fondo
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progresoAnimado)
                        .fillMaxHeight()
                        .background(Color(0xFF2DD4BF)) // Turquesa del wireframe
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "${listaConItems.itemsCompletados()}/${listaConItems.totalItems()}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

