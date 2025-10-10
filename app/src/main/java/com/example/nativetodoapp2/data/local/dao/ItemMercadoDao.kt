package com.example.nativetodoapp2.data.local.dao

import androidx.room.*
import com.example.nativetodoapp2.data.model.ItemMercado
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de la tabla items_mercado
 */
@Dao
interface ItemMercadoDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(item: ItemMercado): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarVarios(items: List<ItemMercado>)
    
    @Update
    suspend fun actualizar(item: ItemMercado)
    
    @Delete
    suspend fun eliminar(item: ItemMercado)
    
    @Query("SELECT * FROM items_mercado WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Long): ItemMercado?
    
    @Query("SELECT * FROM items_mercado WHERE listaId = :listaId ORDER BY marcado ASC, orden ASC")
    fun obtenerItemsDeLista(listaId: Long): Flow<List<ItemMercado>>
    
    @Query("SELECT * FROM items_mercado WHERE listaId = :listaId AND marcado = 0 ORDER BY orden ASC")
    fun obtenerItemsNoMarcados(listaId: Long): Flow<List<ItemMercado>>
    
    @Query("SELECT * FROM items_mercado WHERE listaId = :listaId AND marcado = 1 ORDER BY orden ASC")
    fun obtenerItemsMarcados(listaId: Long): Flow<List<ItemMercado>>
    
    @Query("UPDATE items_mercado SET marcado = :marcado WHERE id = :itemId")
    suspend fun actualizarMarcado(itemId: Long, marcado: Boolean)
    
    @Query("DELETE FROM items_mercado WHERE listaId = :listaId")
    suspend fun eliminarTodosDeLista(listaId: Long)
    
    @Query("DELETE FROM items_mercado WHERE listaId = :listaId AND marcado = 1")
    suspend fun eliminarMarcadosDeLista(listaId: Long)
    
    @Query("SELECT COUNT(*) FROM items_mercado WHERE listaId = :listaId")
    suspend fun contarItems(listaId: Long): Int
    
    @Query("SELECT COUNT(*) FROM items_mercado WHERE listaId = :listaId AND marcado = 1")
    suspend fun contarItemsMarcados(listaId: Long): Int
}

