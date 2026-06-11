package com.dosismed.ui.formulas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dosismed.di.AppContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormulasScreen(
    onBack: () -> Unit,
    onNueva: () -> Unit,
    onEditar: (Long) -> Unit,
) {
    var busqueda by remember { mutableStateOf("") }
    val repo = AppContainer.formulaRepository
    val formulas by (if (busqueda.isBlank()) repo.observarTodas() else repo.buscar(busqueda))
        .collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fórmulas / Tratamientos") },
                navigationIcon = { com.dosismed.ui.components.BackButton(onBack) },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNueva) { Icon(Icons.Filled.Add, contentDescription = "Nueva fórmula") }
        },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                label = { Text("Buscar por padecimiento o síntoma") },
                leadingIcon = { Icon(Icons.Filled.Search, null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            )

            if (formulas.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        if (busqueda.isBlank()) "No hay fórmulas. Toca + para agregar."
                        else "Sin resultados para \"$busqueda\".",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(bottom = 16.dp)) {
                    items(formulas, key = { it.id }) { f ->
                        Card(modifier = Modifier.fillMaxWidth().clickable { onEditar(f.id) }) {
                            Column(Modifier.padding(16.dp)) {
                                Text(f.padecimiento, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                                if (f.sintomas.isNotBlank()) {
                                    Text(f.sintomas, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Spacer(Modifier.height(6.dp))
                                Text("Tratamiento: ${f.medicamentos}", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
    }
}
