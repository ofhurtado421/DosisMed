package com.dosismed.domain.model

/**
 * Modelo de dominio de un medicamento del vademécum.
 *
 * La dosificación se expresa en mg/kg (estándar clínico). A partir de la
 * concentración del producto la app calcula la cantidad para cualquier peso:
 *  - Líquidos (jarabe, suspensión, gotas, solución): concentración en mg / mL,
 *    el resultado se expresa en mL.
 *  - Sólidos (tableta, cápsula): concentración en mg por unidad (volumen de
 *    referencia = 1), el resultado se expresa en mg y número de unidades.
 */
data class Medicamento(
    val id: Long = 0L,
    val nombre: String,
    val tipo: TipoMedicamento,
    val forma: FormaFarmaceutica,
    val poblacion: Poblacion,
    val concentracionMg: Double,
    val volumenReferenciaMl: Double,
    val dosisMgPorKgMin: Double,
    val dosisMgPorKgMax: Double,
    val frecuenciaHorasMin: Int,
    val frecuenciaHorasMax: Int,
    val maxDosisDia: Int,
    val recomendaciones: String,
) {
    /** True si la presentación es sólida (tableta o cápsula). */
    val esSolido: Boolean
        get() = forma == FormaFarmaceutica.TABLETA || forma == FormaFarmaceutica.CAPSULA

    /** Unidad en la que se expresa la dosis: "mL" (líquidos) o "tableta"/"cápsula" (sólidos). */
    val unidadDosis: String
        get() = when (forma) {
            FormaFarmaceutica.TABLETA -> "tableta"
            FormaFarmaceutica.CAPSULA -> "cápsula"
            else -> "mL"
        }

    /**
     * Miligramos por unidad de dosificación:
     *  - Líquidos: mg por cada mL.
     *  - Sólidos: mg por cada tableta/cápsula (volumen de referencia = 1).
     */
    val mgPorUnidad: Double
        get() = if (volumenReferenciaMl > 0.0) concentracionMg / volumenReferenciaMl else 0.0

    /** Alias por compatibilidad (igual a mgPorUnidad). */
    val mgPorMl: Double get() = mgPorUnidad

    /** Etiqueta legible de concentración, p.ej. "150 mg / 5 mL" o "500 mg por tableta". */
    val concentracionLabel: String
        get() = if (esSolido) "${concentracionMg.limpio()} mg por ${unidadDosis}"
        else "${concentracionMg.limpio()} mg / ${volumenReferenciaMl.limpio()} mL"

    val frecuenciaLabel: String
        get() = if (frecuenciaHorasMin == frecuenciaHorasMax) "Cada $frecuenciaHorasMin horas"
        else "Cada $frecuenciaHorasMin–$frecuenciaHorasMax horas"
}

/** Formatea un Double quitando ".0" cuando es entero. */
fun Double.limpio(decimales: Int = 1): String {
    val redondeado = kotlin.math.round(this * 10.0) / 10.0
    return if (redondeado % 1.0 == 0.0) redondeado.toLong().toString()
    else redondeado.toString()
}
