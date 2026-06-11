package com.dosismed.data

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.dosismed.db.DosisMedDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver =
        AndroidSqliteDriver(
            schema = DosisMedDatabase.Schema,
            context = context,
            name = "dosismed.db",
        )
}
