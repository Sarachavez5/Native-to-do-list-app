package com.MercandoAndoApp.MercandoAndo.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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

    // --- NUEVO --- Estados para el modo selección
    val modoSeleccion by listasViewModel.modoSeleccion.collectAsState()
    val listasSeleccionadasIds by listasViewModel.listasSeleccionadasIds.collectAsState()
    var mostrarDialogoBorrado by remember { mutableStateOf(false) }


    // Mostrar mensajes toast
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        listasViewModel.mensajeToast.collect { mensaje ->
            snackbarHostState.showSnackbar(mensaje)
        }
    }

    Scaffold(
        topBar = {
            // --- NUEVO --- TopAppBar dinámico según el modo selección
            TopAppBar(
                title = {
                    if (modoSeleccion) {
                        Text("${listasSeleccionadasIds.size} seleccionada(s)")
                    } else {
                        Text("Mis listas")
                    }
                },
                actions = {
                    if (modoSeleccion) {
                        // Acciones para el modo selección
                        IconButton(onClick = { listasViewModel.seleccionarTodo() }) {
                            Icon(Icons.Default.SelectAll, "Seleccionar todo")
                        }
                        IconButton(onClick = { mostrarDialogoBorrado = true }) {
                            Icon(Icons.Default.Delete, "Borrar seleccionadas")
                        }
                        IconButton(onClick = { listasViewModel.desactivarModoSeleccion() }) {
                            Icon(Icons.Default.Close, "Cancelar selección")
                        }
                    } else {
                        // Acciones normales
                        IconButton(onClick = { listasViewModel.activarModoSeleccion() }) {
                            Icon(Icons.Default.DoneAll, "Activar selección")
                        }
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
                                leadingIcon = { Icon(Icons.Default.DeleteSweep, null) }
                            )
                        }
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
                    Text("No tienes listas", style = MaterialTheme.typography.titleLarge)
                    Text("Crea tu primera lista de mercado", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                items(listas, key = { it.lista.id }) { listaConItems ->
                    TarjetaLista(
                        listaConItems = listaConItems,
                        // --- NUEVO --- Lógica de click y selección
                        enModoSeleccion = modoSeleccion,
                        estaSeleccionada = listasSeleccionadasIds.contains(listaConItems.lista.id),
                        onClick = {
                            if (modoSeleccion) {
                                listasViewModel.seleccionarLista(listaConItems.lista.id)
                            } else {
                                navController.navigate(Rutas.DetalleLista.crearRuta(listaConItems.lista.id))
                            }
                        },
                        onMenuClick = {
                            if (!modoSeleccion) { // Solo mostrar menú si no estamos en selección
                                listaSeleccionada = listaConItems
                            }
                        }
                    )
                }
            }
        }
    }

    if (mostrarDialogoNuevaLista) {
        //... (Diálogo de nueva lista sin cambios)
    }

    // --- NUEVO --- Diálogo para confirmar borrado múltiple
    if (mostrarDialogoBorrado) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoBorrado = false },
            title = { Text("Confirmar borrado") },
            text = { Text("¿Mover ${listasSeleccionadasIds.size} lista(s) a la papelera?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        listasViewModel.eliminarListasSeleccionadas()
                        mostrarDialogoBorrado = false
                    }
                ) { Text("BORRAR") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoBorrado = false }) { Text("CANCELAR") }
            }
        )
    }

    //... (Diálogo de opciones individuales sin cambios)
}

@Composable
fun TarjetaLista(
    listaConItems: ListaConItems,
    enModoSeleccion: Boolean,    // --- NUEVO ---
    estaSeleccionada: Boolean, // --- NUEVO ---
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
            containerColor = MaterialTheme.colorScheme.surface // Cambia si está seleccionada
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        // --- NUEVO --- Borde para resaltar selección
        border = if (estaSeleccionada) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- NUEVO --- Checkbox condicional
            if (enModoSeleccion) {
                Checkbox(
                    checked = estaSeleccionada,
                    onCheckedChange = { onClick() },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = listaConItems.lista.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = if (estaSeleccionada) FontWeight.Bold else FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(Color(0xFFE5E7EB))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progresoAnimado)
                            .fillMaxHeight()
                            .background(Color(0xFF2DD4BF))
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${listaConItems.itemsCompletados()}/${listaConItems.totalItems()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // --- NUEVO --- El IconButton ocupa espacio pero es invisible si está en modo selección
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier.animateContentSize(),
                enabled = !enModoSeleccion
            ) {
                if(!enModoSeleccion) {
                    Icon(Icons.Default.MoreVert, "Opciones")
                }
            }
        }
    }
}
