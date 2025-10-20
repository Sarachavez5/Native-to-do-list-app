package com.MercandoAndoApp.MercandoAndo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de Room para representar una lista de mercado
 */
@Entity(tableName = "listas_mercado")
data class ListaMercado(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val fechaCreacion: Long = System.currentTimeMillis(),
    val eliminada: Boolean = false,
    val usuarioId: Long = 1 // FK al usuario propietario
)

