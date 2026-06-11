package com.dosismed.di

import com.dosismed.data.DatabaseDriverFactory
import com.dosismed.data.FormulaRepository
import com.dosismed.data.MedicamentoRepository
import com.dosismed.data.SeedData
import com.dosismed.data.createDatabase
import com.dosismed.db.DosisMedDatabase

/**
 * Inyección de dependencias sencilla (service locator) compartida por
 * Android y Desktop. Cada plataforma llama a [init] con su propia fábrica
 * de driver al arrancar.
 */
object AppContainer {

    private var database: DosisMedDatabase? = null

    val medicamentoRepository: MedicamentoRepository by lazy {
        MedicamentoRepository(requireDb())
    }

    val formulaRepository: FormulaRepository by lazy {
        FormulaRepository(requireDb())
    }

    fun init(driverFactory: DatabaseDriverFactory) {
        if (database != null) return
        val db = createDatabase(driverFactory)
        SeedData.poblarSiVacio(db)
        database = db
    }

    private fun requireDb(): DosisMedDatabase =
        database ?: error("AppContainer.init(...) no fue llamado antes de usar los repositorios")
}
