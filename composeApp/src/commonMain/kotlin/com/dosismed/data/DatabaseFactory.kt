package com.dosismed.data

import com.dosismed.db.DosisMedDatabase

fun createDatabase(driverFactory: DatabaseDriverFactory): DosisMedDatabase {
    return DosisMedDatabase(driverFactory.createDriver())
}
