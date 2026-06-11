package com.dosismed.ui.formulas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import com.dosismed.di.AppContainer
import com.dosismed.domain.model.Formula
import com.dosismed.ui.components.BackButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormFormulaScreen(
    formulaId: Long,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val repo = AppContainer.formulaRepository
    val esNueva = formulaId == 0L

    var padecimiento by remember { mutableStateOf("") }
    var sintomas by remember { mutableStateOf("") }
    var medicamentos by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    LaunchedEffect(formulaId) {
        if (!esNueva) {
            repo.obtener(formulaId)?.let { f ->
                padecimiento = f.padecimiento
                sintomas = f.sintomas
                medicamentos = f.medicamentos
                descripcion = f.descripcion
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (esNueva) "Nueva fórmula" else "Editar fórmula") },
                navigationIcon = { BackButton(onBack) },
                actions = {
                    if (!esNueva) {
                        IconButton(onClick = { scope.launch { repo.eliminar(formulaId); onBack() } }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                        }
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(padecimiento, { padecimiento = it }, label = { Text("Padecimiento / enfermedad") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(sintomas, { sintomas = it }, label = { Text("Síntomas que presenta el paciente") }, minLines = 2, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(medicamentos, { medicamentos = it }, label = { Text("Medicamentos recetados") }, minLines = 2, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(descripcion, { descripcion = it }, label = { Text("Descripción / indicaciones") }, minLines = 4, modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(4.dp))
            Button(
                onClick = {
                    val f = Formula(
                        id = formulaId,
                        padecimiento = padecimiento.trim(),
                        sintomas = sintomas.trim(),
                        medicamentos = medicamentos.trim(),
                        descripcion = descripcion.trim(),
                    )
                    scope.launch { repo.guardar(f); onBack() }
                },
                enabled = padecimiento.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Guardar", modifier = Modifier.padding(vertical = 4.dp))
            }
            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Cancelar") }
            Spacer(Modifier.height(24.dp))
        }
    }
}
