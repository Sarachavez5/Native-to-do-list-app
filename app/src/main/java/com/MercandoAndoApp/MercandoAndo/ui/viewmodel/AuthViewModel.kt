package com.MercandoAndoApp.MercandoAndo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.MercandoAndoApp.MercandoAndo.data.datastore.PreferenciasManager
import com.MercandoAndoApp.MercandoAndo.data.model.Usuario
import com.MercandoAndoApp.MercandoAndo.data.repository.MercadoRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel para manejar autenticación y sesión de usuario
 */
class AuthViewModel(
    private val repository: MercadoRepository,
    private val preferencias: PreferenciasManager
) : ViewModel() {
    
    private val _estadoAuth = MutableStateFlow<AuthEstado>(AuthEstado.NoAutenticado)
    val estadoAuth: StateFlow<AuthEstado> = _estadoAuth.asStateFlow()
    
    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()
    
    private val _modoOscuro = MutableStateFlow(false)
    val modoOscuro: StateFlow<Boolean> = _modoOscuro.asStateFlow()
    
    init {
        verificarSesionGuardada()
        observarModoOscuro()
    }
    
    private fun verificarSesionGuardada() {
        viewModelScope.launch {
            preferencias.usuarioIdFlow.collect { usuarioId ->
                if (usuarioId != null) {
                    val usuario = repository.obtenerUsuario(usuarioId)
                    if (usuario != null) {
                        _usuarioActual.value = usuario
                        _estadoAuth.value = AuthEstado.Autenticado
                    }
                }
            }
        }
    }
    
    private fun observarModoOscuro() {
        viewModelScope.launch {
            preferencias.modoOscuroFlow.collect { oscuro ->
                _modoOscuro.value = oscuro
            }
        }
    }
    
    fun registrar(
        nombre: String,
        apellidos: String,
        correo: String,
        contraseña: String,
        confirmarContraseña: String
    ) {
        viewModelScope.launch {
            // Validaciones
            if (nombre.isBlank() || apellidos.isBlank() || correo.isBlank() || contraseña.isBlank()) {
                _estadoAuth.value = AuthEstado.Error("Todos los campos son obligatorios")
                return@launch
            }
            
            if (!correo.contains("@")) {
                _estadoAuth.value = AuthEstado.Error("Correo inválido")
                return@launch
            }
            
            if (contraseña.length < 6) {
                _estadoAuth.value = AuthEstado.Error("La contraseña debe tener al menos 6 caracteres")
                return@launch
            }
            
            if (contraseña != confirmarContraseña) {
                _estadoAuth.value = AuthEstado.Error("Las contraseñas no coinciden")
                return@launch
            }
            
            _estadoAuth.value = AuthEstado.Cargando
            
            val resultado = repository.registrarUsuario(nombre, apellidos, correo, contraseña)
            
            resultado.fold(
                onSuccess = { usuario ->
                    _usuarioActual.value = usuario
                    preferencias.guardarUsuarioId(usuario.id)
                    _estadoAuth.value = AuthEstado.Autenticado
                },
                onFailure = { error ->
                    _estadoAuth.value = AuthEstado.Error(error.message ?: "Error al registrar")
                }
            )
        }
    }
    
    fun iniciarSesion(correo: String, contraseña: String) {
        viewModelScope.launch {
            // Usuario de prueba
            if (correo == "test@test.com" && contraseña == "password") {
                val usuarioDePrueba = Usuario(id = 0, nombre = "Usuario", apellidos = "De Prueba", correo = "test@test.com", contraseñaHash = "")
                _usuarioActual.value = usuarioDePrueba
                preferencias.guardarUsuarioId(usuarioDePrueba.id)
                _estadoAuth.value = AuthEstado.Autenticado
                return@launch
            }

            // Validaciones
            if (correo.isBlank() || contraseña.isBlank()) {
                _estadoAuth.value = AuthEstado.Error("Todos los campos son obligatorios")
                return@launch
            }

            _estadoAuth.value = AuthEstado.Cargando

            val usuario = repository.autenticarUsuario(correo, contraseña)

            if (usuario != null) {
                _usuarioActual.value = usuario
                preferencias.guardarUsuarioId(usuario.id)
                _estadoAuth.value = AuthEstado.Autenticado
            } else {
                _estadoAuth.value = AuthEstado.Error("Correo o contraseña incorrectos")
            }
        }
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            preferencias.cerrarSesion()
            _usuarioActual.value = null
            _estadoAuth.value = AuthEstado.NoAutenticado
        }
    }

    fun limpiarError() {
        if (_estadoAuth.value is AuthEstado.Error) {
            _estadoAuth.value = AuthEstado.NoAutenticado
        }
    }

    fun alternarModoOscuro() {
        viewModelScope.launch {
            val nuevoEstado = !_modoOscuro.value
            preferencias.alternarModoOscuro(nuevoEstado)
        }
    }
}

/**
 * Estados posibles de autenticación
 */
sealed class AuthEstado {
    data object NoAutenticado : AuthEstado()
    data object Cargando : AuthEstado()
    data object Autenticado : AuthEstado()
    data class Error(val mensaje: String) : AuthEstado()
}
