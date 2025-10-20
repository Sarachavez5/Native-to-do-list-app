package com.example.nativetodoapp2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nativetodoapp2.data.model.*
import com.example.nativetodoapp2.data.repository.MercadoRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel para manejar las listas de mercado
 */
class ListasViewModel(
    private val repository: MercadoRepository,
    private val usuarioId: Long
) : ViewModel() {

    // --- ESTADOS PARA EL MODO DE SELECCIÓN (NUEVO) ---

    private val _modoSeleccion = MutableStateFlow(false)
    val modoSeleccion: StateFlow<Boolean> = _modoSeleccion.asStateFlow()

    private val _listasSeleccionadasIds = MutableStateFlow<Set<Long>>(emptySet())
    val listasSeleccionadasIds: StateFlow<Set<Long>> = _listasSeleccionadasIds.asStateFlow()

    // --- FIN DE ESTADOS PARA EL MODO DE SELECCIÓN ---

    // Listas activas con sus items
    val listasActivas: StateFlow<List<ListaConItems>> = repository
        .obtenerListasActivas(usuarioId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Listas en la papelera
    val listasEliminadas: StateFlow<List<ListaMercado>> = repository
        .obtenerListasEliminadas(usuarioId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _listaSeleccionada = MutableStateFlow<ListaConItems?>(null)
    val listaSeleccionada: StateFlow<ListaConItems?> = _listaSeleccionada.asStateFlow()

    private val _mensajeToast = MutableSharedFlow<String>()
    val mensajeToast: SharedFlow<String> = _mensajeToast.asSharedFlow()

    // --- FUNCIONES PARA MANEJAR MODO DE SELECCIÓN (NUEVO) ---

    fun activarModoSeleccion() {
        _modoSeleccion.value = true
    }

    fun desactivarModoSeleccion() {
        _modoSeleccion.value = false
        _listasSeleccionadasIds.value = emptySet() // Limpiar selección al salir
    }

    fun seleccionarLista(listaId: Long) {
        val actuales = _listasSeleccionadasIds.value.toMutableSet()
        if (actuales.contains(listaId)) {
            actuales.remove(listaId)
        } else {
            actuales.add(listaId)
        }
        _listasSeleccionadasIds.value = actuales
    }

    fun seleccionarTodo() {
        val todosLosIds = listasActivas.value.map { it.lista.id }.toSet()
        _listasSeleccionadasIds.value = todosLosIds
    }

    fun deseleccionarTodo() {
        _listasSeleccionadasIds.value = emptySet()
    }

    fun eliminarListasSeleccionadas() {
        viewModelScope.launch {
            val ids = _listasSeleccionadasIds.value
            if (ids.isEmpty()) {
                _mensajeToast.emit("No hay listas seleccionadas")
                return@launch
            }
            ids.forEach { repository.marcarListaComoEliminada(it) }
            _mensajeToast.emit("${ids.size} lista(s) movida(s) a la papelera")
            desactivarModoSeleccion() // Salir del modo selección después de borrar
        }
    }

    // --- FIN DE FUNCIONES PARA MANEJAR MODO DE SELECCIÓN ---

    fun crearLista(nombre: String) {
        viewModelScope.launch {
            if (nombre.isBlank()) {
                _mensajeToast.emit("El nombre no puede estar vacío")
                return@launch
            }
            repository.crearLista(nombre, usuarioId)
            _mensajeToast.emit("Lista creada: $nombre")
        }
    }

    fun renombrarLista(listaId: Long, nuevoNombre: String) {
        viewModelScope.launch {
            if (nuevoNombre.isBlank()) {
                _mensajeToast.emit("El nombre no puede estar vacío")
                return@launch
            }
            repository.renombrarLista(listaId, nuevoNombre)
            _mensajeToast.emit("Lista renombrada")
        }
    }

    fun copiarListaCompleta(listaId: Long, nombreOriginal: String) {
        viewModelScope.launch {
            val nuevoNombre = "$nombreOriginal (Copia)"
            repository.copiarLista(listaId, nuevoNombre)
            _mensajeToast.emit("Lista copiada")
        }
    }

    fun copiarSoloNoComprados(listaId: Long, nombreOriginal: String) {
        viewModelScope.launch {
            val nuevoNombre = "$nombreOriginal (No comprados)"
            repository.copiarLista(listaId, nuevoNombre, soloNoMarcados = true)
            _mensajeToast.emit("Artículos no comprados copiados")
        }
    }

    fun copiarSoloComprados(listaId: Long, nombreOriginal: String) {
        viewModelScope.launch {
            val nuevoNombre = "$nombreOriginal (Comprados)"
            repository.copiarLista(listaId, nuevoNombre, soloMarcados = true)
            _mensajeToast.emit("Artículos comprados copiados")
        }
    }

    fun eliminarLista(listaId: Long) {
        viewModelScope.launch {
            repository.marcarListaComoEliminada(listaId)
            _mensajeToast.emit("Lista movida a la papelera")
        }
    }

    fun restaurarLista(listaId: Long) {
        viewModelScope.launch {
            repository.restaurarLista(listaId)
            _mensajeToast.emit("Lista restaurada")
        }
    }

    fun eliminarListaPermanentemente(listaId: Long) {
        viewModelScope.launch {
            repository.eliminarListaPermanentemente(listaId)
            _mensajeToast.emit("Lista eliminada permanentemente")
        }
    }

    fun vaciarPapelera() {
        viewModelScope.launch {
            repository.vaciarPapelera()
            _mensajeToast.emit("Papelera vaciada")
        }
    }

    fun cargarListaDetalle(listaId: Long) {
        viewModelScope.launch {
            val lista = repository.obtenerListaConItems(listaId)
            _listaSeleccionada.value = lista
        }
    }

    fun agregarItem(
        listaId: Long,
        nombre: String,
        categoria: CategoriaProducto,
        cantidad: Int = 1
    ) {
        viewModelScope.launch {
            repository.agregarItem(listaId, nombre, categoria, cantidad)
            cargarListaDetalle(listaId) // Recargar
            _mensajeToast.emit("$nombre agregado")
        }
    }

    fun marcarItem(itemId: Long, marcado: Boolean, listaId: Long) {
        viewModelScope.launch {
            repository.marcarItem(itemId, marcado)
            cargarListaDetalle(listaId) // Recargar
        }
    }

    fun eliminarItem(item: ItemMercado, listaId: Long) {
        viewModelScope.launch {
            repository.eliminarItem(item)
            cargarListaDetalle(listaId) // Recargar
            _mensajeToast.emit("${item.nombre} eliminado")
        }
    }

    fun eliminarItemsMarcados(listaId: Long) {
        viewModelScope.launch {
            repository.eliminarItemsMarcados(listaId)
            cargarListaDetalle(listaId) // Recargar
            _mensajeToast.emit("Items marcados eliminados")
        }
    }
}
