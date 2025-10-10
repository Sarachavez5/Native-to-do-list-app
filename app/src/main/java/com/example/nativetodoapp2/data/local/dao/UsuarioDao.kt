package com.example.nativetodoapp2.data.local.dao

import androidx.room.*
import com.example.nativetodoapp2.data.model.Usuario
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de la tabla usuarios
 */
@Dao
interface UsuarioDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: Usuario): Long
    
    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun obtenerPorCorreo(correo: String): Usuario?
    
    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Long): Usuario?
    
    @Query("SELECT * FROM usuarios WHERE correo = :correo AND contraseñaHash = :contraseñaHash LIMIT 1")
    suspend fun autenticar(correo: String, contraseñaHash: String): Usuario?
    
    @Update
    suspend fun actualizar(usuario: Usuario)
    
    @Delete
    suspend fun eliminar(usuario: Usuario)
    
    @Query("SELECT * FROM usuarios")
    fun obtenerTodos(): Flow<List<Usuario>>
}

