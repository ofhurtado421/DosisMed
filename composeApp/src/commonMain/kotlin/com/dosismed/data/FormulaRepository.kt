package com.dosismed.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.dosismed.db.DosisMedDatabase
import com.dosismed.domain.model.Formula
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import com.dosismed.db.Formula as FormulaEntity

class FormulaRepository(db: DosisMedDatabase) {

    private val queries = db.formulaQueries

    fun observarTodas(): Flow<List<Formula>> =
        queries.selectAll().asFlow().mapToList(Dispatchers.Default).map { rows ->
            rows.map { it.toDomain() }
        }

    fun buscar(q: String): Flow<List<Formula>> =
        queries.search(q).asFlow().mapToList(Dispatchers.Default).map { rows ->
            rows.map { it.toDomain() }
        }

    suspend fun obtener(id: Long): Formula? = withContext(Dispatchers.Default) {
        queries.selectById(id).executeAsOneOrNull()?.toDomain()
    }

    suspend fun guardar(f: Formula): Unit = withContext(Dispatchers.Default) {
        if (f.id == 0L) {
            queries.insert(f.padecimiento, f.sintomas, f.medicamentos, f.descripcion)
        } else {
            queries.update(f.padecimiento, f.sintomas, f.medicamentos, f.descripcion, f.id)
        }
    }

    suspend fun eliminar(id: Long): Unit = withContext(Dispatchers.Default) {
        queries.deleteById(id)
    }
}

private fun FormulaEntity.toDomain(): Formula = Formula(
    id = id,
    padecimiento = padecimiento,
    sintomas = sintomas,
    medicamentos = medicamentos,
    descripcion = descripcion,
)
