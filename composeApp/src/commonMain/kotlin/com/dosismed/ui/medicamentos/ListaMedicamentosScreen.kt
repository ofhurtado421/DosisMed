package com.dosismed.ui.medicamentos

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import com.dosismed.domain.model.Poblacion
import com.dosismed.ui.components.BackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaMedicamentosScreen(
    onBack: () -> Unit,
    onNuevo: () -> Unit,
    onEditar: (Long) -> Unit,
) {
    val todos by AppContainer.medicamentoRepository.observarTodos().collectAsState(initial = emptyList())
    var busqueda by remember { mutableStateOf("") }
    var filtroPoblacion by remember { mutableStateOf<Poblacion?>(null) }

    val filtrados = todos.filter { m ->
        (busqueda.isBlank() || m.nombre.contains(busqueda, ignoreCase = true) ||
            m.tipo.label.contains(busqueda, ignoreCase = true)) &&
            (filtroPoblacion == null || m.poblacion == filtroPoblacion || m.poblacion == Poblacion.AMBOS)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Vademécum") }, navigationIcon = { BackButton(onBack) }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onNuevo) {
                Icon(Icons.Filled.Add, contentDescription = "Nuevo medicamento")
            }
        },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                label = { Text("Buscar medicamento") },
                leadingIcon = { Icon(Icons.Filled.Search, null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilterChip(
                    selected = filtroPoblacion == null,
                    onClick = { filtroPoblacion = null },
                    label = { Text("Todos") },
                )
                FilterChip(
                    selected = filtroPoblacion == Poblacion.PEDIATRICO,
                    onClick = { filtroPoblacion = Poblacion.PEDIATRICO },
                    label = { Text("Pediátrico") },
                )
                FilterChip(
                    selected = filtroPoblacion == Poblacion.ADULTO,
                    onClick = { filtroPoblacion = Poblacion.ADULTO },
                    label = { Text("Adulto") },
                )
            }

            if (filtrados.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay medicamentos. Toca + para agregar.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(bottom = 16.dp)) {
                    items(filtrados, key = { it.id }) { m ->
                        Card(modifier = Modifier.fillMaxWidth().clickable { onEditar(m.id) }) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(m.nombre, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                                Text(
                                    "${m.tipo.label} · ${m.forma.label} · ${m.concentracionLabel}",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                Spacer(Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    AssistChip(onClick = { onEditar(m.id) }, label = { Text(m.poblacion.label) }, shape = RoundedCornerShape(8.dp))
                                    AssistChip(onClick = { onEditar(m.id) }, label = { Text("${m.dosisMgPorKgMin.toInt()}–${m.dosisMgPorKgMax.toInt()} mg/kg") }, shape = RoundedCornerShape(8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
