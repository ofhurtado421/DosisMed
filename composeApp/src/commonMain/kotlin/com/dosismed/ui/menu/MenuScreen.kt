package com.dosismed.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class MenuItem(
    val titulo: String,
    val descripcion: String,
    val icono: ImageVector,
    val onClick: () -> Unit,
)

@Composable
fun MenuScreen(
    onMedicamentos: () -> Unit,
    onCalculadora: () -> Unit,
    onFormulas: () -> Unit,
    onAcerca: () -> Unit,
    onLogout: () -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    val items = listOf(
        MenuItem("Vademécum", "Administrar medicamentos y sus dosis", Icons.Filled.Medication, onMedicamentos),
        MenuItem("Calculadora de dosis", "Calcular por peso y edad del paciente", Icons.Filled.Calculate, onCalculadora),
        MenuItem("Fórmulas / Tratamientos", "Buscar por padecimiento o síntoma", Icons.Filled.MedicalServices, onFormulas),
        MenuItem("Acerca de", "Información de la app y créditos", Icons.Filled.Info, onAcerca),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(scheme.primary, scheme.primaryContainer))),
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {

            // Barra superior con botón de cerrar sesión (vuelve al inicio de usuario)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = onLogout) {
                    Icon(
                        Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Cerrar sesión",
                        tint = scheme.onPrimary,
                    )
                }
            }

            // Encabezado centrado
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Surface(
                    shape = CircleShape,
                    color = scheme.onPrimary.copy(alpha = 0.18f),
                    modifier = Modifier.size(84.dp),
                ) {
                    Icon(
                        Icons.Filled.MedicalServices,
                        contentDescription = null,
                        tint = scheme.onPrimary,
                        modifier = Modifier.padding(20.dp),
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text("DosisMed", color = scheme.onPrimary, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Text("¿Qué deseas hacer?", color = scheme.onPrimary.copy(alpha = 0.85f), fontSize = 15.sp)
            }

            Spacer(Modifier.height(24.dp))

            // Tarjetas grandes que ocupan el resto de la pantalla, repartidas por igual
            Column(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items.forEach { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth().weight(1f).clickable { item.onClick() },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = scheme.surface),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize().padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = scheme.primaryContainer,
                                modifier = Modifier.size(56.dp),
                            ) {
                                Icon(
                                    item.icono,
                                    contentDescription = null,
                                    tint = scheme.primary,
                                    modifier = Modifier.padding(14.dp),
                                )
                            }
                            Spacer(Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.titulo, fontWeight = FontWeight.Bold, fontSize = 19.sp, color = scheme.onSurface)
                                Text(item.descripcion, fontSize = 13.sp, color = scheme.onSurfaceVariant)
                            }
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = scheme.primary)
                        }
                    }
                }
            }
        }
    }
}
