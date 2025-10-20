package com.MercandoAndoApp.MercandoAndo.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Gestor de preferencias usando DataStore para datos simples como sesión
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferencias")

class PreferenciasManager(private val context: Context) {
    
    companion object {
        private val USUARIO_ID = longPreferencesKey("usuario_id")
        private val MODO_OSCURO = booleanPreferencesKey("modo_oscuro")
    }
    
    val usuarioIdFlow: Flow<Long?> = context.dataStore.data.map { preferences ->
        preferences[USUARIO_ID]
    }
    
    val modoOscuroFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[MODO_OSCURO] ?: false
    }
    
    suspend fun guardarUsuarioId(id: Long) {
        context.dataStore.edit { preferences ->
            preferences[USUARIO_ID] = id
        }
    }
    
    suspend fun cerrarSesion() {
        context.dataStore.edit { preferences ->
            preferences.remove(USUARIO_ID)
        }
    }
    
    suspend fun alternarModoOscuro(activado: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[MODO_OSCURO] = activado
        }
    }
}

