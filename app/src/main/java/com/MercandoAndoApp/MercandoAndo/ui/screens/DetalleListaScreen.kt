package com.MercandoAndoApp.MercandoAndo.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.MercandoAndoApp.MercandoAndo.data.model.ItemMercado
import com.MercandoAndoApp.MercandoAndo.ui.navigation.Rutas
import com.MercandoAndoApp.MercandoAndo.ui.viewmodel.AuthViewModel
import com.MercandoAndoApp.MercandoAndo.ui.viewmodel.ListasViewModel
import com.MercandoAndoApp.MercandoAndo.ui.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleListaScreen(
    navController: NavController,
    listaId: Long,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val usuarioActual by authViewModel.usuarioActual.collectAsState()
    
    val listasViewModel: ListasViewModel = viewModel(
        factory = ViewModelFactory(context, usuarioActual?.id)
    )
    
    val listaConItems by listasViewModel.listaSeleccionada.collectAsState()
    var seccionMarcadosExpandida by remember { mutableStateOf(false) }
    
    LaunchedEffect(listaId) {
        listasViewModel.cargarListaDetalle(listaId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(listaConItems?.lista?.nombre ?: "Cargando...") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Menú de opciones */ }) {
                        Icon(Icons.Default.MoreVert, "Más opciones")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Rutas.AgregarProductos.crearRuta(listaId))
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, "Añadir")
            }
        }
    ) { paddingValues ->
        listaConItems?.let { lista ->
            if (lista.items.isEmpty()) {
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
                            text = "¡Empieza tu mercado!",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Toca el botón de añadir para comenzar",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    // Items agrupados por categoría (solo no marcados)
                    val itemsPorCategoria = lista.itemsPorCategoria()
                    
                    itemsPorCategoria.forEach { (categoria, items) ->
                        item {
                            Text(
                                text = categoria.nombreMostrar,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        
                        items(items, key = { it.id }) { item ->
                            ItemMercadoRow(
                                item = item,
                                onCheckedChange = { marcado ->
                                    listasViewModel.marcarItem(item.id, marcado, listaId)
                                },
                                onDelete = {
                                    listasViewModel.eliminarItem(item, listaId)
                                }
                            )
                        }
                        
                        item { Spacer(modifier = Modifier.height(8.dp)) }
                    }
                    
                    // Sección de items marcados (colapsable)
                    val itemsMarcados = lista.itemsMarcados()
                    if (itemsMarcados.isNotEmpty()) {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { seccionMarcadosExpandida = !seccionMarcadosExpandida }
                                            .padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = if (seccionMarcadosExpandida) 
                                                    Icons.Default.KeyboardArrowDown 
                                                else 
                                                    Icons.Default.KeyboardArrowUp,
                                                contentDescription = if (seccionMarcadosExpandida) "Colapsar" else "Expandir"
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Marcados (${itemsMarcados.size})",
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                        }
                                    }
                                    
                                    AnimatedVisibility(visible = seccionMarcadosExpandida) {
                                        Column {
                                            itemsMarcados.forEach { item ->
                                                ItemMercadoRow(
                                                    item = item,
                                                    onCheckedChange = { marcado ->
                                                        listasViewModel.marcarItem(item.id, marcado, listaId)
                                                    },
                                                    onDelete = {
                                                        listasViewModel.eliminarItem(item, listaId)
                                                    },
                                                    modifier = Modifier.padding(horizontal = 16.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    // Espaciado inferior para el FAB
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
fun ItemMercadoRow(
    item: ItemMercado,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var mostrarMenuOpciones by remember { mutableStateOf(false) }
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.marcado,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.secondary
            )
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = item.nombre,
            style = MaterialTheme.typography.bodyLarge,
            textDecoration = if (item.marcado) TextDecoration.LineThrough else null,
            color = if (item.marcado) 
                MaterialTheme.colorScheme.onSurfaceVariant 
            else 
                MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        
        Box {
            IconButton(onClick = { mostrarMenuOpciones = true }) {
                Icon(Icons.Default.MoreVert, "Opciones", tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            
            DropdownMenu(
                expanded = mostrarMenuOpciones,
                onDismissRequest = { mostrarMenuOpciones = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Eliminar") },
                    onClick = {
                        onDelete()
                        mostrarMenuOpciones = false
                    },
                    leadingIcon = { Icon(Icons.Default.Delete, null) }
                )
            }
        }
    }
}

