# 🛒 App de Lista de Mercado - Android Nativo

Una aplicación nativa de Android desarrollada con **Kotlin** y **Jetpack Compose** para gestionar listas de mercado de forma intuitiva y eficiente.

## 📱 Características Principales

### ✅ Autenticación de Usuario
- Registro de nuevos usuarios con validación de datos
- Inicio de sesión seguro con contraseñas hasheadas (SHA-256)
- Persistencia de sesión con DataStore Preferences

### 🛍️ Gestión de Listas
- Crear múltiples listas de mercado
- Renombrar y eliminar listas
- Copiar listas completas o parciales (solo marcados/no marcados)
- Ver progreso visual con barra de progreso
- Sistema de papelera para recuperar listas eliminadas

### 📦 Gestión de Items
- Agregar items a las listas desde catálogo predefinido
- Items organizados por categorías:
  - Lácteos
  - Proteínas
  - Frutas y Verduras
  - Panadería
  - Despensa
  - Bebidas
  - Higiene Personal
  - Limpieza
  - Snacks
  - Otros
- Marcar items como completados
- Items completados se mueven automáticamente a sección "Marcados"
- Sección de marcados colapsable
- Eliminar items individuales

### 🎨 Diseño y UX
- Material Design 3
- Tema claro y oscuro
- Animaciones suaves
- Navegación intuitiva con Bottom Navigation Bar
- Colores según wireframes (Turquesa/Teal primario)

### 👤 Perfil y Configuración
- Visualización de datos del usuario
- Alternar entre modo claro y oscuro
- Cerrar sesión

### ℹ️ Pantalla de Créditos
- Información del equipo de desarrollo
- Enlaces a GitHub
- Correos de contacto

## 🏗️ Arquitectura

### Patrón MVVM (Model-View-ViewModel)
- **Model**: Entidades de Room Database
- **View**: Composables de Jetpack Compose
- **ViewModel**: Gestión de estados con StateFlow

### Capas de la Aplicación

```
├── data/
│   ├── model/              # Modelos de datos (Entities)
│   ├── local/
│   │   ├── dao/            # Data Access Objects
│   │   └── MercadoDatabase.kt
│   ├── repository/         # Repositorio único
│   └── datastore/          # Preferencias
├── ui/
│   ├── screens/            # Pantallas Composable
│   ├── components/         # Componentes reutilizables
│   ├── navigation/         # Sistema de navegación
│   ├── viewmodel/          # ViewModels
│   └── theme/              # Tema y colores
└── MainActivity.kt
```

## 🛠️ Tecnologías Utilizadas

### Core
- **Kotlin** 2.0.21
- **Jetpack Compose** - UI moderna y declarativa
- **Material Design 3** - Componentes de UI

### Persistencia
- **Room Database** 2.6.1 - Base de datos local
- **DataStore Preferences** 1.0.0 - Preferencias simples

### Arquitectura
- **ViewModel** - Gestión de estados
- **StateFlow / SharedFlow** - Flujos reactivos
- **Navigation Compose** 2.7.7 - Navegación

### Seguridad
- **SHA-256** - Hash de contraseñas

### Procesamiento
- **KSP** - Procesador de anotaciones de Kotlin
- **Coroutines** 1.7.3 - Programación asíncrona

## 📋 Requisitos del Sistema

- **Android Studio**: Hedgehog (2023.1.1) o superior
- **Kotlin**: 2.0.21
- **SDK Mínimo**: API 24 (Android 7.0)
- **SDK Target**: API 36

## 🚀 Instalación y Configuración

### 1. Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/NativeToDoApp.git
cd NativeToDoApp
```

### 2. Abrir en Android Studio

1. Abre Android Studio
2. Selecciona "Open an Existing Project"
3. Navega a la carpeta del proyecto
4. Espera a que Gradle sincronice las dependencias

### 3. Ejecutar la Aplicación

1. Conecta un dispositivo Android o inicia un emulador
2. Haz clic en el botón "Run" (▶️) o presiona `Shift + F10`
3. La app se instalará y ejecutará automáticamente

## 📱 Pantallas de la Aplicación

### 1. **Login**
- Iniciar sesión con correo y contraseña
- Enlace a pantalla de registro
- Enlace de "¿Olvidaste tu contraseña?"

### 2. **Registro**
- Formulario con: Nombre, Apellidos, Correo, Contraseña, Confirmar Contraseña
- Validación de campos
- Enlace para volver a login

### 3. **Listas** (Home)
- Vista de todas las listas creadas
- Tarjetas con nombre, progreso visual y fracción (X/Y)
- Botón FAB para crear nueva lista
- Menú de opciones por lista:
  - Renombrar
  - Copiar (completa, solo marcados, solo no marcados)
  - Borrar
- Menú superior para acceder a papelera

### 4. **Detalle de Lista**
- Items agrupados por categoría
- Checkbox para marcar items
- Items marcados se mueven automáticamente a sección "Marcados"
- Sección de marcados colapsable
- Botón FAB para agregar items
- Estado vacío con mensaje motivador

### 5. **Agregar Productos**
- Barra de búsqueda para productos personalizados
- Tabs: Popular / Reciente
- Productos organizados por categoría
- Clic en producto lo agrega automáticamente

### 6. **Perfil**
- Avatar con iniciales del usuario
- Nombre completo y correo
- Botón "Editar perfil"
- Botón "Cerrar sesión"
- Acceso a ajustes (modo oscuro)

### 7. **Créditos**
- Logo de la aplicación
- Tarjetas de integrantes del equipo
- Información de contacto
- Enlace al repositorio de GitHub

### 8. **Papelera**
- Listas eliminadas recuperables
- Botones para restaurar o eliminar permanentemente
- Botón "Vaciar" para limpiar toda la papelera
- Estado vacío cuando no hay listas eliminadas

## 🎨 Paleta de Colores

### Colores Principales
- **Primario (Turquesa)**: `#2DD4BF`
- **Secundario (Verde)**: `#22C55E`
- **Error (Rojo)**: `#EF4444`

### Modo Oscuro
- **Fondo**: `#0B1220`
- **Contenedor**: `#1E293B`
- **Texto**: `#E5E7EB`
- **Bordes**: `#334155`

### Modo Claro
- **Fondo**: `#F8FAFC`
- **Contenedor**: `#FFFFFF`
- **Texto**: `#0F172A`
- **Bordes**: `#E2E8F0`

## 🗄️ Modelo de Datos

### Usuario
```kotlin
data class Usuario(
    val id: Long,
    val nombre: String,
    val apellidos: String,
    val correo: String,
    val contraseñaHash: String
)
```

### ListaMercado
```kotlin
data class ListaMercado(
    val id: Long,
    val nombre: String,
    val fechaCreacion: Long,
    val eliminada: Boolean,
    val usuarioId: Long
)
```

### ItemMercado
```kotlin
data class ItemMercado(
    val id: Long,
    val listaId: Long,
    val nombre: String,
    val categoria: String,
    val marcado: Boolean,
    val cantidad: Int,
    val precio: Double?,
    val notas: String?
)
```

## 🔄 Flujo de Navegación

```
Login → Registro
  ↓
(Autenticado)
  ↓
Listas ←→ Perfil ←→ Créditos
  ↓
Detalle de Lista
  ↓
Agregar Productos

Listas → Papelera
```

## 🧪 Testing

### Tests Unitarios
Los tests se encuentran en `app/src/test/java/`

```bash
./gradlew test
```

### Tests de Instrumentación
Los tests de UI se encuentran en `app/src/androidTest/java/`

```bash
./gradlew connectedAndroidTest
```

## 📄 Licencia

Este proyecto fue desarrollado como parte de un proyecto académico.

## 👥 Equipo de Desarrollo

- **Sara Chavez** - Desarrolladora
  - Email: sara@gmail.com
  
- **Cristian Usme** - Desarrollador
  - Email: cristian@gmail.com

## 🔗 Enlaces

- **Repositorio**: [github.com/GitHub-141](https://github.com/GitHub-141)

## 📝 Notas de Desarrollo

### Características Implementadas
✅ Autenticación completa con validación  
✅ CRUD de listas de mercado  
✅ CRUD de items por lista  
✅ Agrupación automática por categorías  
✅ Sistema de items marcados con movimiento automático  
✅ Papelera con restauración  
✅ Copiar listas (completa, marcados, no marcados)  
✅ Modo oscuro/claro persistente  
✅ Animaciones y transiciones suaves  
✅ Validaciones de formularios  
✅ Persistencia con Room Database  
✅ Navegación con Navigation Compose  
✅ Material Design 3  

### Mejoras Futuras Sugeridas
- 🔄 Sincronización en la nube
- 🔔 Notificaciones y recordatorios
- 📊 Estadísticas de compras
- 🤝 Compartir listas con otros usuarios
- 💰 Gestión de presupuesto
- 📸 Escaneo de códigos de barras
- 🏪 Sugerencias basadas en tiendas
- 🌍 Múltiples idiomas

---

**Desarrollado con ❤️ usando Kotlin y Jetpack Compose**
