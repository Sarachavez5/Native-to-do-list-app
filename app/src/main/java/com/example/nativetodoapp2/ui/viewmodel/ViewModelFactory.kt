package com.example.nativetodoapp2.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nativetodoapp2.data.datastore.PreferenciasManager
import com.example.nativetodoapp2.data.local.MercadoDatabase
import com.example.nativetodoapp2.data.repository.MercadoRepository

/**
 * Factory para crear ViewModels con dependencias
 */
class ViewModelFactory(
    private val context: Context,
    private val usuarioId: Long? = null
) : ViewModelProvider.Factory {
    
    private val database by lazy { MercadoDatabase.getInstance(context) }
    private val repository by lazy { MercadoRepository(database) }
    private val preferencias by lazy { PreferenciasManager(context) }
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(repository, preferencias) as T
            }
            modelClass.isAssignableFrom(ListasViewModel::class.java) -> {
                requireNotNull(usuarioId) { "usuarioId requerido para ListasViewModel" }
                ListasViewModel(repository, usuarioId) as T
            }
            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}

