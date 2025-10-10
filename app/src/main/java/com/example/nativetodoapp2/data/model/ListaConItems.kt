package com.example.nativetodoapp2.data.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Clase de relación para obtener una lista con todos sus items
 */
data class ListaConItems(
    @Embedded val lista: ListaMercado,
    @Relation(
        parentColumn = "id",
        entityColumn = "listaId"
    )
    val items: List<ItemMercado>
) {
    /**
     * Calcula el total de items en la lista
     */
    fun totalItems(): Int = items.size

    /**
     * Calcula cuántos items están marcados como completados
     */
    fun itemsCompletados(): Int = items.count { it.marcado }

    /**
     * Calcula el porcentaje de progreso (0-100)
     */
    fun progresoPercentual(): Int {
        return if (totalItems() == 0) 0
        else ((itemsCompletados().toFloat() / totalItems()) * 100).toInt()
    }

    /**
     * Obtiene los items agrupados por categoría (solo los no marcados)
     */
    fun itemsPorCategoria(): Map<CategoriaProducto, List<ItemMercado>> {
        return items
            .filter { !it.marcado }
            .groupBy { CategoriaProducto.fromNombre(it.categoria) }
    }

    /**
     * Obtiene solo los items marcados
     */
    fun itemsMarcados(): List<ItemMercado> {
        return items.filter { it.marcado }
    }
}

