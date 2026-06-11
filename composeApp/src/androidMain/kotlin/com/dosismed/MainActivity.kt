package com.dosismed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dosismed.data.DatabaseDriverFactory
import com.dosismed.di.AppContainer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Inicializa la base de datos y los repositorios para esta plataforma.
        AppContainer.init(DatabaseDriverFactory(applicationContext))
        setContent {
            App()
        }
    }
}
