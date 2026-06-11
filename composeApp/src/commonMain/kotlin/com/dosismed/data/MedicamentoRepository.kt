package com.dosismed.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.dosismed.db.DosisMedDatabase
import com.dosismed.domain.model.FormaFarmaceutica
import com.dosismed.domain.model.Medicamento
import com.dosismed.domain.model.Poblacion
import com.dosismed.domain.model.TipoMedicamento
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import com.dosismed.db.Medicamento as MedicamentoEntity

class MedicamentoRepository(db: DosisMedDatabase) {

    private val queries = db.medicamentoQueries

    fun observarTodos(): Flow<List<Medicamento>> =
        queries.selectAll().asFlow().mapToList(Dispatchers.Default).map { rows ->
            rows.map { it.toDomain() }
        }

    suspend fun obtener(id: Long): Medicamento? = withContext(Dispatchers.Default) {
        queries.selectById(id).executeAsOneOrNull()?.toDomain()
    }

    suspend fun guardar(m: Medicamento): Unit = withContext(Dispatchers.Default) {
        if (m.id == 0L) {
            queries.insert(
                nombre = m.nombre,
                tipo = m.tipo.name,
                forma = m.forma.name,
                poblacion = m.poblacion.name,
                concentracionMg = m.concentracionMg,
                volumenReferenciaMl = m.volumenReferenciaMl,
                dosisMgPorKgMin = m.dosisMgPorKgMin,
                dosisMgPorKgMax = m.dosisMgPorKgMax,
                frecuenciaHorasMin = m.frecuenciaHorasMin.toLong(),
                frecuenciaHorasMax = m.frecuenciaHorasMax.toLong(),
                maxDosisDia = m.maxDosisDia.toLong(),
                recomendaciones = m.recomendaciones,
            )
        } else {
            queries.update(
                nombre = m.nombre,
                tipo = m.tipo.name,
                forma = m.forma.name,
                poblacion = m.poblacion.name,
                concentracionMg = m.concentracionMg,
                volumenReferenciaMl = m.volumenReferenciaMl,
                dosisMgPorKgMin = m.dosisMgPorKgMin,
                dosisMgPorKgMax = m.dosisMgPorKgMax,
                frecuenciaHorasMin = m.frecuenciaHorasMin.toLong(),
                frecuenciaHorasMax = m.frecuenciaHorasMax.toLong(),
                maxDosisDia = m.maxDosisDia.toLong(),
                recomendaciones = m.recomendaciones,
                id = m.id,
            )
        }
    }

    suspend fun eliminar(id: Long): Unit = withContext(Dispatchers.Default) {
        queries.deleteById(id)
    }
}

private fun MedicamentoEntity.toDomain(): Medicamento = Medicamento(
    id = id,
    nombre = nombre,
    tipo = TipoMedicamento.fromName(tipo),
    forma = FormaFarmaceutica.fromName(forma),
    poblacion = Poblacion.fromName(poblacion),
    concentracionMg = concentracionMg,
    volumenReferenciaMl = volumenReferenciaMl,
    dosisMgPorKgMin = dosisMgPorKgMin,
    dosisMgPorKgMax = dosisMgPorKgMax,
    frecuenciaHorasMin = frecuenciaHorasMin.toInt(),
    frecuenciaHorasMax = frecuenciaHorasMax.toInt(),
    maxDosisDia = maxDosisDia.toInt(),
    recomendaciones = recomendaciones,
)
