package com.MercandoAndoApp.MercandoAndo.data.local.dao

import androidx.room.*
import com.MercandoAndoApp.MercandoAndo.data.model.ListaMercado
import com.MercandoAndoApp.MercandoAndo.data.model.ListaConItems
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de la tabla listas_mercado
 */
@Dao
interface ListaMercadoDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(lista: ListaMercado): Long
    
    @Update
    suspend fun actualizar(lista: ListaMercado)
    
    @Delete
    suspend fun eliminar(lista: ListaMercado)
    
    @Query("SELECT * FROM listas_mercado WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Long): ListaMercado?
    
    @Query("SELECT * FROM listas_mercado WHERE usuarioId = :usuarioId AND eliminada = 0 ORDER BY fechaCreacion DESC")
    fun obtenerListasActivas(usuarioId: Long): Flow<List<ListaMercado>>
    
    @Query("SELECT * FROM listas_mercado WHERE usuarioId = :usuarioId AND eliminada = 1 ORDER BY fechaCreacion DESC")
    fun obtenerListasEliminadas(usuarioId: Long): Flow<List<ListaMercado>>
    
    @Transaction
    @Query("SELECT * FROM listas_mercado WHERE id = :listaId LIMIT 1")
    suspend fun obtenerListaConItems(listaId: Long): ListaConItems?
    
    @Transaction
    @Query("SELECT * FROM listas_mercado WHERE usuarioId = :usuarioId AND eliminada = 0 ORDER BY fechaCreacion DESC")
    fun obtenerListasConItems(usuarioId: Long): Flow<List<ListaConItems>>
    
    @Query("UPDATE listas_mercado SET eliminada = 1 WHERE id = :listaId")
    suspend fun marcarComoEliminada(listaId: Long)
    
    @Query("UPDATE listas_mercado SET eliminada = 0 WHERE id = :listaId")
    suspend fun restaurar(listaId: Long)
    
    @Query("DELETE FROM listas_mercado WHERE id = :listaId")
    suspend fun eliminarPermanentemente(listaId: Long)
    
    @Query("DELETE FROM listas_mercado WHERE eliminada = 1")
    suspend fun vaciarPapelera()
}

