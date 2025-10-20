package com.MercandoAndoApp.MercandoAndo.data.repository

import com.MercandoAndoApp.MercandoAndo.data.local.MercadoDatabase
import com.MercandoAndoApp.MercandoAndo.data.model.*
import kotlinx.coroutines.flow.Flow
import java.security.MessageDigest

/**
 * Repositorio principal que maneja toda la lógica de datos
 */
class MercadoRepository(private val database: MercadoDatabase) {
    
    private val usuarioDao = database.usuarioDao()
    private val listaDao = database.listaMercadoDao()
    private val itemDao = database.itemMercadoDao()
    
    // ============ Operaciones de Usuario ============
    
    /**
     * Hashea una contraseña usando SHA-256
     */
    private fun hashContraseña(contraseña: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(contraseña.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
    
    suspend fun registrarUsuario(
        nombre: String,
        apellidos: String,
        correo: String,
        contraseña: String
    ): Result<Usuario> {
        return try {
            // Verificar si el correo ya existe
            val usuarioExistente = usuarioDao.obtenerPorCorreo(correo)
            if (usuarioExistente != null) {
                return Result.failure(Exception("El correo ya está registrado"))
            }
            
            val usuario = Usuario(
                nombre = nombre,
                apellidos = apellidos,
                correo = correo,
                contraseñaHash = hashContraseña(contraseña)
            )
            val id = usuarioDao.insertar(usuario)
            Result.success(usuario.copy(id = id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun autenticarUsuario(correo: String, contraseña: String): Usuario? {
        return usuarioDao.autenticar(correo, hashContraseña(contraseña))
    }
    
    suspend fun obtenerUsuario(id: Long): Usuario? {
        return usuarioDao.obtenerPorId(id)
    }
    
    suspend fun actualizarUsuario(usuario: Usuario) {
        usuarioDao.actualizar(usuario)
    }
    
    // ============ Operaciones de Lista ============
    
    suspend fun crearLista(nombre: String, usuarioId: Long): Long {
        val lista = ListaMercado(nombre = nombre, usuarioId = usuarioId)
        return listaDao.insertar(lista)
    }
    
    suspend fun actualizarLista(lista: ListaMercado) {
        listaDao.actualizar(lista)
    }
    
    suspend fun renombrarLista(listaId: Long, nuevoNombre: String) {
        val lista = listaDao.obtenerPorId(listaId)
        lista?.let {
            listaDao.actualizar(it.copy(nombre = nuevoNombre))
        }
    }
    
    suspend fun marcarListaComoEliminada(listaId: Long) {
        listaDao.marcarComoEliminada(listaId)
    }
    
    suspend fun restaurarLista(listaId: Long) {
        listaDao.restaurar(listaId)
    }
    
    suspend fun eliminarListaPermanentemente(listaId: Long) {
        listaDao.eliminarPermanentemente(listaId)
    }
    
    suspend fun vaciarPapelera() {
        listaDao.vaciarPapelera()
    }
    
    fun obtenerListasActivas(usuarioId: Long): Flow<List<ListaConItems>> {
        return listaDao.obtenerListasConItems(usuarioId)
    }
    
    fun obtenerListasEliminadas(usuarioId: Long): Flow<List<ListaMercado>> {
        return listaDao.obtenerListasEliminadas(usuarioId)
    }
    
    suspend fun obtenerListaConItems(listaId: Long): ListaConItems? {
        return listaDao.obtenerListaConItems(listaId)
    }
    
    suspend fun copiarLista(
        listaId: Long,
        nuevoNombre: String,
        soloMarcados: Boolean = false,
        soloNoMarcados: Boolean = false
    ): Long? {
        val listaOriginal = listaDao.obtenerListaConItems(listaId) ?: return null
        
        // Crear nueva lista
        val nuevaListaId = crearLista(nuevoNombre, listaOriginal.lista.usuarioId)
        
        // Copiar items según el filtro
        val itemsACopiar = when {
            soloMarcados -> listaOriginal.items.filter { it.marcado }
            soloNoMarcados -> listaOriginal.items.filter { !it.marcado }
            else -> listaOriginal.items
        }
        
        val nuevosItems = itemsACopiar.map { item ->
            item.copy(id = 0, listaId = nuevaListaId, marcado = false)
        }
        
        itemDao.insertarVarios(nuevosItems)
        return nuevaListaId
    }
    
    // ============ Operaciones de Items ============
    
    suspend fun agregarItem(
        listaId: Long,
        nombre: String,
        categoria: CategoriaProducto,
        cantidad: Int = 1,
        precio: Double? = null,
        notas: String? = null
    ): Long {
        val item = ItemMercado(
            listaId = listaId,
            nombre = nombre,
            categoria = categoria.nombreMostrar,
            cantidad = cantidad,
            precio = precio,
            notas = notas,
            orden = itemDao.contarItems(listaId)
        )
        return itemDao.insertar(item)
    }
    
    suspend fun actualizarItem(item: ItemMercado) {
        itemDao.actualizar(item)
    }
    
    suspend fun marcarItem(itemId: Long, marcado: Boolean) {
        itemDao.actualizarMarcado(itemId, marcado)
    }
    
    suspend fun eliminarItem(item: ItemMercado) {
        itemDao.eliminar(item)
    }
    
    suspend fun eliminarItemsMarcados(listaId: Long) {
        itemDao.eliminarMarcadosDeLista(listaId)
    }
    
    fun obtenerItemsDeLista(listaId: Long): Flow<List<ItemMercado>> {
        return itemDao.obtenerItemsDeLista(listaId)
    }
    
    suspend fun obtenerEstadisticasLista(listaId: Long): Pair<Int, Int> {
        val total = itemDao.contarItems(listaId)
        val marcados = itemDao.contarItemsMarcados(listaId)
        return Pair(marcados, total)
    }
}

