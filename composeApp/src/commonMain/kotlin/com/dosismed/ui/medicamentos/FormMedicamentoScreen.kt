package com.dosismed.ui.medicamentos

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dosismed.di.AppContainer
import com.dosismed.domain.model.FormaFarmaceutica
import com.dosismed.domain.model.Medicamento
import com.dosismed.domain.model.Poblacion
import com.dosismed.domain.model.TipoMedicamento
import com.dosismed.ui.components.BackButton
import com.dosismed.ui.components.EnumDropdown
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormMedicamentoScreen(
    medicamentoId: Long,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val repo = AppContainer.medicamentoRepository
    val esNuevo = medicamentoId == 0L

    var nombre by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf(TipoMedicamento.ANALGESICO) }
    var forma by remember { mutableStateOf(FormaFarmaceutica.JARABE) }
    var poblacion by remember { mutableStateOf(Poblacion.PEDIATRICO) }
    var concentracion by remember { mutableStateOf("") }
    var volumenRef by remember { mutableStateOf("") }
    var mgkgMin by remember { mutableStateOf("") }
    var mgkgMax by remember { mutableStateOf("") }
    var frecMin by remember { mutableStateOf("") }
    var frecMax by remember { mutableStateOf("") }
    var maxDia by remember { mutableStateOf("") }
    var recomendaciones by remember { mutableStateOf("") }

    // Sólido = tableta o cápsula (dosis en mg por unidad). Líquido = mg por mL.
    val esSolido = forma == FormaFarmaceutica.TABLETA || forma == FormaFarmaceutica.CAPSULA
    val unidad = when (forma) {
        FormaFarmaceutica.TABLETA -> "tableta"
        FormaFarmaceutica.CAPSULA -> "cápsula"
        else -> "mL"
    }

    LaunchedEffect(medicamentoId) {
        if (!esNuevo) {
            repo.obtener(medicamentoId)?.let { m ->
                nombre = m.nombre
                tipo = m.tipo
                forma = m.forma
                poblacion = m.poblacion
                concentracion = m.concentracionMg.toString()
                volumenRef = m.volumenReferenciaMl.toString()
                mgkgMin = m.dosisMgPorKgMin.toString()
                mgkgMax = m.dosisMgPorKgMax.toString()
                frecMin = m.frecuenciaHorasMin.toString()
                frecMax = m.frecuenciaHorasMax.toString()
                maxDia = m.maxDosisDia.toString()
                recomendaciones = m.recomendaciones
            }
        }
    }

    fun guardar() {
        val solido = forma == FormaFarmaceutica.TABLETA || forma == FormaFarmaceutica.CAPSULA
        val m = Medicamento(
            id = medicamentoId,
            nombre = nombre.trim(),
            tipo = tipo,
            forma = forma,
            poblacion = poblacion,
            concentracionMg = concentracion.toDoubleOrNull() ?: 0.0,
            // En sólidos el volumen de referencia es 1 (mg por unidad).
            volumenReferenciaMl = if (solido) 1.0 else (volumenRef.toDoubleOrNull() ?: 5.0),
            dosisMgPorKgMin = mgkgMin.toDoubleOrNull() ?: 0.0,
            dosisMgPorKgMax = mgkgMax.toDoubleOrNull() ?: (mgkgMin.toDoubleOrNull() ?: 0.0),
            frecuenciaHorasMin = frecMin.toIntOrNull() ?: 6,
            frecuenciaHorasMax = frecMax.toIntOrNull() ?: (frecMin.toIntOrNull() ?: 6),
            maxDosisDia = maxDia.toIntOrNull() ?: 4,
            recomendaciones = recomendaciones.trim(),
        )
        scope.launch {
            repo.guardar(m)
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (esNuevo) "Nuevo medicamento" else "Editar medicamento") },
                navigationIcon = { BackButton(onBack) },
                actions = {
                    if (!esNuevo) {
                        IconButton(onClick = {
                            scope.launch { repo.eliminar(medicamentoId); onBack() }
                        }) { Icon(Icons.Filled.Delete, contentDescription = "Eliminar") }
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre del medicamento") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            EnumDropdown("Tipo", TipoMedicamento.entries, tipo, { it.label }, { tipo = it })
            EnumDropdown("Forma farmacéutica", FormaFarmaceutica.entries, forma, { it.label }, { forma = it })
            EnumDropdown("Población", Poblacion.entries, poblacion, { it.label }, { poblacion = it })

            if (esSolido) {
                Text("Concentración por unidad", style = MaterialTheme.typography.titleSmall)
                OutlinedTextField(
                    concentracion, { concentracion = it },
                    label = { Text("mg por $unidad") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                )
            } else {
                Text("Concentración del producto", style = MaterialTheme.typography.titleSmall)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(concentracion, { concentracion = it }, label = { Text("mg") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.weight(1f))
                    OutlinedTextField(volumenRef, { volumenRef = it }, label = { Text("por mL") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.weight(1f))
                }
            }

            Text("Dosis (mg por kg de peso)", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(mgkgMin, { mgkgMin = it }, label = { Text("mg/kg mín") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.weight(1f))
                OutlinedTextField(mgkgMax, { mgkgMax = it }, label = { Text("mg/kg máx") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.weight(1f))
            }

            Text("Frecuencia (horas entre tomas)", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(frecMin, { frecMin = it }, label = { Text("cada (mín)") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f))
                OutlinedTextField(frecMax, { frecMax = it }, label = { Text("cada (máx)") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f))
            }
            OutlinedTextField(maxDia, { maxDia = it }, label = { Text("Máximo de dosis en 24 h") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(recomendaciones, { recomendaciones = it }, label = { Text("Recomendaciones") }, minLines = 3, modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(4.dp))
            Button(onClick = { guardar() }, enabled = nombre.isNotBlank(), modifier = Modifier.fillMaxWidth()) {
                Text("Guardar", modifier = Modifier.padding(vertical = 4.dp))
            }
            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Cancelar") }
            Spacer(Modifier.height(24.dp))
        }
    }
}
