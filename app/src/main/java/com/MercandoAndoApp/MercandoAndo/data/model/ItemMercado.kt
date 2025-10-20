package com.MercandoAndoApp.MercandoAndo.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidad de Room para representar un item dentro de una lista de mercado
 */
@Entity(
    tableName = "items_mercado",
    foreignKeys = [
        ForeignKey(
            entity = ListaMercado::class,
            parentColumns = ["id"],
            childColumns = ["listaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("listaId")]
)
data class ItemMercado(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val listaId: Long,
    val nombre: String,
    val categoria: String, // Nombre de la categoría
    val marcado: Boolean = false,
    val cantidad: Int = 1,
    val precio: Double? = null,
    val notas: String? = null,
    val orden: Int = 0 // Para ordenar los items
)

