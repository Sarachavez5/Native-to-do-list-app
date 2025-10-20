package com.MercandoAndoApp.MercandoAndo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de Room para representar un usuario
 */
@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val apellidos: String,
    val correo: String,
    val contraseñaHash: String // Contraseña hasheada con SHA-256
)

