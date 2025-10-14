package com.example.nativetodoapp2.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nativetodoapp2.ui.components.BottomNavigationBar
import com.example.nativetodoapp2.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditosScreen(
    navController: NavController
) {
    val context = LocalContext.current
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Créditos") }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Logo con mismo diseño que login/registro (cuadrado con bordes redondeados)
            Surface(
                modifier = Modifier.size(100.dp),
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.primary
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Logo",
                        modifier = Modifier.size(60.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Desarrollado por",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "Equipo de desarrollo",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Tarjeta Sara Chavez
            TarjetaIntegrante(
                nombre = "Sara Chavez",
                rol = "UX y Frontend",
                correo = "sarachavez500@gmail.com",
                githubUrl = "https://github.com/Sarachavez5",
                iniciales = "SC",
                colorAvatar = MaterialTheme.colorScheme.primary,
                onGitHubClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Sarachavez5"))
                    context.startActivity(intent)
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Tarjeta Cristian Usme
            TarjetaIntegrante(
                nombre = "Cristian Usme",
                rol = "Backend",
                correo = "cristian1213u@gmail.com",
                githubUrl = "https://github.com/Cristian-Usme",
                iniciales = "CU",
                colorAvatar = Color(0xFF667eea), // Morado según wireframe
                onGitHubClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Cristian-Usme"))
                    context.startActivity(intent)
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Tarjeta María José Gómez
            TarjetaIntegrante(
                nombre = "María José Gómez",
                rol = "Backend y Diseño",
                correo = "gomezmajo2004@gmail.com",
                githubUrl = "https://github.com/mariagomezv",
                iniciales = "MG",
                colorAvatar = Color(0xFFf093fb), // Rosa según wireframe
                onGitHubClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/mariagomezv"))
                    context.startActivity(intent)
                }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Sección del repositorio
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF667eea) // Morado del wireframe
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Repositorio del Proyecto",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Explora el código fuente",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Sarachavez5/Native-to-do-list-app"))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF667eea)
                        )
                    ) {
                        Text("Ver en GitHub →")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "github.com/Sarachavez5/Native-to-do-list-app",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun TarjetaIntegrante(
    nombre: String,
    rol: String,
    correo: String,
    githubUrl: String,
    iniciales: String,
    colorAvatar: Color,
    onGitHubClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(colorAvatar),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = iniciales,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column {
                    Text(
                        text = nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = rol,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Correo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Icon(
                    Icons.Default.Email,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = correo,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // GitHub
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clickable(onClick = onGitHubClick)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_github),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "GitHub",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

