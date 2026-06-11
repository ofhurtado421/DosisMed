package com.dosismed.ui.calculadora

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dosismed.di.AppContainer
import com.dosismed.domain.model.Medicamento
import com.dosismed.domain.model.ResultadoDosis
import com.dosismed.domain.usecase.DosisCalculator
import com.dosismed.ui.components.BackButton
import com.dosismed.ui.components.EnumDropdown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculadoraScreen(onBack: () -> Unit) {
    val medicamentos by AppContainer.medicamentoRepository.observarTodos().collectAsState(initial = emptyList())
    var seleccionado by remember { mutableStateOf<Medicamento?>(null) }
    var peso by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf<ResultadoDosis?>(null) }

    // Mantiene seleccionado el primero disponible.
    if (seleccionado == null && medicamentos.isNotEmpty()) seleccionado = medicamentos.first()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Calculadora de dosis") }, navigationIcon = { BackButton(onBack) }) },
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (medicamentos.isEmpty()) {
                Text("Primero agrega medicamentos en el Vademécum.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                return@Column
            }

            val sel = seleccionado!!
            EnumDropdown(
                label = "Medicamento",
                opciones = medicamentos,
                seleccion = sel,
                etiqueta = { "${it.nombre} (${it.concentracionLabel})" },
                onSeleccion = { seleccionado = it; resultado = null },
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(peso, { peso = it; resultado = null }, label = { Text("Peso (kg)") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.weight(1f))
                OutlinedTextField(edad, { edad = it; resultado = null }, label = { Text("Edad (años)") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.weight(1f))
            }

            Button(
                onClick = {
                    val p = peso.replace(',', '.').toDoubleOrNull() ?: 0.0
                    val e = edad.replace(',', '.').toDoubleOrNull() ?: 0.0
                    resultado = DosisCalculator.calcular(sel, p, e)
                },
                enabled = peso.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Calcular dosis", modifier = Modifier.padding(vertical = 4.dp))
            }

            resultado?.let { r ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Dosis por toma", fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text(r.principalLabel, fontSize = 34.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text("(${r.secundarioLabel})", fontSize = 13.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        HorizontalDivider(Modifier.padding(vertical = 10.dp))
                        FilaDato("Frecuencia", r.frecuencia)
                        FilaDato("Máximo diario", "${r.maxDosisDia} dosis (~${r.cantidadMaxDiaLabel})")
                    }
                }

                if (r.recomendaciones.isNotBlank()) {
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Recomendaciones", fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Text(r.recomendaciones, fontSize = 14.sp)
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Warning, null, tint = MaterialTheme.colorScheme.onErrorContainer)
                            Spacer(Modifier.height(0.dp))
                            Text("  Advertencias", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onErrorContainer)
                        }
                        Spacer(Modifier.height(6.dp))
                        r.advertencias.forEach { adv ->
                            Text("• $adv", fontSize = 13.sp, color = MaterialTheme.colorScheme.onErrorContainer, modifier = Modifier.padding(vertical = 2.dp))
                        }
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FilaDato(etiqueta: String, valor: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(etiqueta, color = MaterialTheme.colorScheme.onPrimaryContainer)
        Text(valor, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}
