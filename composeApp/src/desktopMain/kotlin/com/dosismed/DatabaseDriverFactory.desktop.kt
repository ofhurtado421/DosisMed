package com.dosismed.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.dosismed.db.DosisMedDatabase
import java.io.File

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        // Base de datos persistente en el directorio del usuario.
        val appDir = File(System.getProperty("user.home"), ".dosismed").apply { if (!exists()) mkdirs() }
        val dbFile = File(appDir, "dosismed.db")
        val necesitaEsquema = !dbFile.exists() || dbFile.length() == 0L

        val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}")

        // El driver JDBC no crea el esquema automáticamente: lo creamos la primera vez.
        if (necesitaEsquema) {
            DosisMedDatabase.Schema.create(driver)
        }
        return driver
    }
}
