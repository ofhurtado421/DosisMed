package com.dosismed

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.dosismed.data.DatabaseDriverFactory
import com.dosismed.di.AppContainer

fun main() {
    // Inicializa base de datos y repositorios antes de lanzar la UI.
    AppContainer.init(DatabaseDriverFactory())

    application {
        val state = rememberWindowState(size = DpSize(440.dp, 860.dp))
        Window(
            onCloseRequest = ::exitApplication,
            state = state,
            title = "DosisMed",
        ) {
            App()
        }
    }
}
