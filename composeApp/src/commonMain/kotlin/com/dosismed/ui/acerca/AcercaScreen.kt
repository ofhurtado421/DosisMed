package com.dosismed.ui.acerca

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dosismed.ui.components.BackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcercaScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Acerca de") }, navigationIcon = { BackButton(onBack) }) },
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(96.dp)) {
                Icon(Icons.Filled.MedicalServices, null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.padding(22.dp))
            }
            Spacer(Modifier.height(16.dp))
            Text("DosisMed", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Dosificación clínica asistida", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Versión 1.0", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(Modifier.height(24.dp))
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Descripción", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Aplicación de apoyo para profesionales de la salud. Permite gestionar un " +
                            "vademécum de medicamentos con sus dosis por kilogramo, calcular la dosis " +
                            "exacta según el peso y la edad del paciente, y consultar fórmulas de " +
                            "tratamiento buscando por el padecimiento o los síntomas presentados.",
                        fontSize = 14.sp,
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "Aviso: las dosis calculadas son orientativas y no sustituyen el criterio " +
                            "médico profesional ni la información oficial del fabricante.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Créditos", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(10.dp))
                    Estudiante("Deimar Quiñonez")
                    Spacer(Modifier.height(8.dp))
                    Estudiante("Oscar Fabian Hurtado Hueje")
                }
            }

            Spacer(Modifier.height(24.dp))
            Text(
                "Proyecto académico · Kotlin Multiplatform (Android + Desktop)",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun Estudiante(nombre: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(shape = CircleShape, color = MaterialTheme.colorScheme.secondaryContainer, modifier = Modifier.size(40.dp)) {
            Icon(Icons.Filled.Person, null, tint = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.padding(8.dp))
        }
        Spacer(Modifier.size(12.dp))
        Text(nombre, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}
