package com.MercandoAndoApp.MercandoAndo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.MercandoAndoApp.MercandoAndo.data.model.CategoriaProducto
import com.MercandoAndoApp.MercandoAndo.ui.viewmodel.AuthViewModel
import com.MercandoAndoApp.MercandoAndo.ui.viewmodel.ListasViewModel
import com.MercandoAndoApp.MercandoAndo.ui.viewmodel.ViewModelFactory

// Productos predefinidos por categoría
private val PRODUCTOS_POR_CATEGORIA = mapOf(
    CategoriaProducto.LACTEOS to listOf("Leche", "Queso", "Yogurt", "Mantequilla"),
    CategoriaProducto.PROTEINAS to listOf("Huevos", "Pollo", "Carne", "Pescado", "Atún"),
    CategoriaProducto.FRUTAS_Y_VERDURAS to listOf("Manzanas", "Plátanos", "Tomates", "Lechuga", "Papas", "Zanahorias"),
    CategoriaProducto.PANADERIA to listOf("Pan", "Tortillas", "Pan de molde"),
    CategoriaProducto.DESPENSA to listOf("Arroz", "Pasta", "Frijoles", "Aceite", "Sal", "Azúcar", "Harina"),
    CategoriaProducto.BEBIDAS to listOf("Agua", "Jugo", "Refresco", "Café"),
    CategoriaProducto.HIGIENE_PERSONAL to listOf("Jabón", "Shampoo", "Pasta dental", "Papel higiénico", "Desodorante"),
    CategoriaProducto.LIMPIEZA to listOf("Detergente", "Cloro", "Lavavajillas", "Toallas de papel"),
    CategoriaProducto.SNACKS to listOf("Galletas", "Papas fritas", "Chocolate", "Chicles")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarProductosScreen(
    navController: NavController,
    listaId: Long,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val usuarioActual by authViewModel.usuarioActual.collectAsState()

    val listasViewModel: ListasViewModel = viewModel(
        factory = ViewModelFactory(context, usuarioActual?.id)
    )

    var busqueda by remember { mutableStateOf("") }
    var tabSeleccionado by remember { mutableStateOf(0) }
    var productosSeleccionados by remember { mutableStateOf(setOf<Pair<String, CategoriaProducto>>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Agregar productos")
                        if (productosSeleccionados.isNotEmpty()) {
                            Text(
                                text = "${productosSeleccionados.size} seleccionado(s)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                actions = {
                    if (productosSeleccionados.isNotEmpty()) {
                        TextButton(
                            onClick = {
                                productosSeleccionados.forEach { (nombre, categoria) ->
                                    listasViewModel.agregarItem(
                                        listaId = listaId,
                                        nombre = nombre,
                                        categoria = categoria
                                    )
                                }
                                navController.popBackStack()
                            }
                        ) {
                            Icon(Icons.Default.Check, "Agregar", modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Agregar")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Barra de búsqueda
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = busqueda,
                    onValueChange = { busqueda = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("🔍 Buscar o agregar producto") },
                    singleLine = true
                )

                if (busqueda.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (busqueda.isNotBlank()) {
                                listasViewModel.agregarItem(
                                    listaId = listaId,
                                    nombre = busqueda,
                                    categoria = CategoriaProducto.OTROS
                                )
                                navController.popBackStack()
                            }
                        }
                    ) {
                        Icon(Icons.Default.Add, "Agregar")
                    }
                }
            }

            // Tabs: Popular / Reciente
            TabRow(selectedTabIndex = tabSeleccionado) {
                Tab(
                    selected = tabSeleccionado == 0,
                    onClick = { tabSeleccionado = 0 },
                    text = { Text("Popular") }
                )
                Tab(
                    selected = tabSeleccionado == 1,
                    onClick = { tabSeleccionado = 1 },
                    text = { Text("Reciente") }
                )
            }

            // Lista de productos por categoría
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PRODUCTOS_POR_CATEGORIA.forEach { (categoria, productos) ->
                    item {
                        Text(
                            text = categoria.nombreMostrar,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                        )
                    }

                    items(productos) { producto ->
                        val productoKey = Pair(producto, categoria)
                        val estaSeleccionado = productosSeleccionados.contains(productoKey)

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                productosSeleccionados = if (estaSeleccionado) {
                                    productosSeleccionados - productoKey
                                } else {
                                    productosSeleccionados + productoKey
                                }
                            },
                            colors = CardDefaults.cardColors(
                                containerColor = if (estaSeleccionado)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = producto,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = if (estaSeleccionado)
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    else
                                        MaterialTheme.colorScheme.onSurface
                                )

                                if (estaSeleccionado) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Seleccionado",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Agregar",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}