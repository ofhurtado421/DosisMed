package com.dosismed.domain.model

/** Resultado del cálculo de dosificación para un paciente concreto. */
data class ResultadoDosis(
    val medicamento: String,
    val esSolido: Boolean,
    val unidad: String,            // "mL", "tableta" o "cápsula"
    val cantidadMin: Double,       // mL (líquido) o número de unidades (sólido) por toma
    val cantidadMax: Double,
    val dosisMgMin: Double,
    val dosisMgMax: Double,
    val frecuencia: String,
    val maxDosisDia: Int,
    val cantidadMaxDia: Double,    // mL/día (líquido) o unidades/día (sólido)
    val recomendaciones: String,
    val advertencias: List<String>,
) {
    val dosisMgLabel: String
        get() = if (dosisMgMin == dosisMgMax) "${dosisMgMin.limpio()} mg"
        else "${dosisMgMin.limpio()} – ${dosisMgMax.limpio()} mg"

    val cantidadLabel: String
        get() = if (cantidadMin == cantidadMax) "${cantidadMin.limpio()} ${unidadPlural(cantidadMin)}"
        else "${cantidadMin.limpio()} – ${cantidadMax.limpio()} ${unidadPlural(cantidadMax)}"

    val cantidadMaxDiaLabel: String
        get() = "${cantidadMaxDia.limpio()} ${unidadPlural(cantidadMaxDia)}/día"

    /** Texto grande y principal del resultado (mg para sólidos, mL para líquidos). */
    val principalLabel: String
        get() = if (esSolido) dosisMgLabel else cantidadLabel

    /** Texto secundario, entre paréntesis. */
    val secundarioLabel: String
        get() = if (esSolido) "≈ $cantidadLabel" else "$dosisMgLabel por toma"

    private fun unidadPlural(valor: Double): String =
        if (unidad == "mL") "mL"
        else if (valor == 1.0) unidad else "${unidad}s"
}
