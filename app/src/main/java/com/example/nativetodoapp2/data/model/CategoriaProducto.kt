package com.example.nativetodoapp2.data.model

/**
 * Categorías de productos para organizar los items del mercado
 */
enum class CategoriaProducto(val nombreMostrar: String) {
    LACTEOS("Lácteos"),
    PROTEINAS("Proteínas"),
    HUEVOS("Huevos"),
    FRUTAS_Y_VERDURAS("Frutas y Verduras"),
    PANADERIA("Panadería"),
    DESPENSA("Despensa"),
    BEBIDAS("Bebidas"),
    HIGIENE_PERSONAL("Higiene Personal"),
    LIMPIEZA("Limpieza"),
    SNACKS("Snacks"),
    OTROS("Otros");

    companion object {
        fun fromNombre(nombre: String): CategoriaProducto {
            return entries.find { it.nombreMostrar == nombre } ?: OTROS
        }
    }
}

