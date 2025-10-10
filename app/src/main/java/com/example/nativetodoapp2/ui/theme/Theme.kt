package com.example.nativetodoapp2.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaColorOscuro = darkColorScheme(
    primary = ColorPrimario,
    onPrimary = Color.White,
    primaryContainer = ColorPrimarioVariante,
    onPrimaryContainer = Color.White,
    
    secondary = ColorSecundario,
    onSecondary = Color.White,
    secondaryContainer = ColorSecundario,
    onSecondaryContainer = Color.White,
    
    tertiary = ColorPrimario,
    onTertiary = Color.White,
    
    error = ColorError,
    onError = Color.White,
    
    background = FondoOscuro,
    onBackground = TextoOscuro,
    
    surface = ContenedorOscuro,
    onSurface = TextoOscuro,
    surfaceVariant = BordeOscuro,
    onSurfaceVariant = DesactivadoOscuro,
    
    outline = BordeOscuro,
    outlineVariant = BordeOscuro
)

private val EsquemaColorClaro = lightColorScheme(
    primary = ColorPrimario,
    onPrimary = Color.White,
    primaryContainer = ColorPrimarioVariante,
    onPrimaryContainer = Color.White,
    
    secondary = ColorSecundario,
    onSecondary = Color.White,
    secondaryContainer = ColorSecundario,
    onSecondaryContainer = Color.White,
    
    tertiary = ColorPrimario,
    onTertiary = Color.White,
    
    error = ColorError,
    onError = Color.White,
    
    background = FondoClaro,
    onBackground = TextoClaro,
    
    surface = ContenedorClaro,
    onSurface = TextoClaro,
    surfaceVariant = BordeClaro,
    onSurfaceVariant = DesactivadoClaro,
    
    outline = BordeClaro,
    outlineVariant = BordeClaro
)

@Composable
fun AppMercadoTheme(
    modoOscuro: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val esquemaColor = if (modoOscuro) EsquemaColorOscuro else EsquemaColorClaro

    MaterialTheme(
        colorScheme = esquemaColor,
        typography = Typography,
        content = content
    )
}
