package com.dosismed.data

import app.cash.sqldelight.db.SqlDriver

/**
 * Fábrica de drivers SQL específica de cada plataforma.
 * La implementación real vive en androidMain y desktopMain (expect/actual).
 */
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
