package com.dosismed.navigation

import kotlinx.serialization.Serializable

/** Rutas type-safe de la app, gestionadas en la capa compartida (commonMain). */
sealed interface Route {
    @Serializable data object Splash : Route
    @Serializable data object Login : Route
    @Serializable data object Menu : Route
    @Serializable data object Medicamentos : Route
    @Serializable data class FormMedicamento(val id: Long = 0L) : Route
    @Serializable data object Calculadora : Route
    @Serializable data object Formulas : Route
    @Serializable data class FormFormula(val id: Long = 0L) : Route
    @Serializable data object Acerca : Route
}
