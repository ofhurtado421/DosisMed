package com.dosismed.domain.model

/**
 * Fórmula / tratamiento de referencia. Permite buscar por el padecimiento
 * o síntoma que presenta el paciente y obtener los medicamentos sugeridos.
 */
data class Formula(
    val id: Long = 0L,
    val padecimiento: String,
    val sintomas: String,
    val medicamentos: String,
    val descripcion: String,
)
