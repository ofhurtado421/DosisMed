package com.dosismed.domain.usecase

import com.dosismed.domain.model.Medicamento
import com.dosismed.domain.model.Poblacion
import com.dosismed.domain.model.ResultadoDosis
import kotlin.math.round

/**
 * Motor de cálculo de dosificación (por peso, estándar clínico):
 *   dosis_mg = peso_kg * mg_por_kg
 *
 * Según la presentación:
 *  - Líquidos: cantidad_mL = dosis_mg / (mg por mL)
 *  - Sólidos:  n.º unidades = dosis_mg / (mg por tableta/cápsula); además se
 *    muestra la dosis en mg, que es como se prescribe en adultos.
 */
object DosisCalculator {

    fun calcular(medicamento: Medicamento, pesoKg: Double, edadAnios: Double): ResultadoDosis {
        val advertencias = mutableListOf<String>()

        val mgPorUnidad = medicamento.mgPorUnidad
        val dosisMgMin = pesoKg * medicamento.dosisMgPorKgMin
        val dosisMgMax = pesoKg * medicamento.dosisMgPorKgMax
        val cantMin = if (mgPorUnidad > 0) dosisMgMin / mgPorUnidad else 0.0
        val cantMax = if (mgPorUnidad > 0) dosisMgMax / mgPorUnidad else 0.0

        // Sólidos: redondeo a 0.5 unidad (más realista). Líquidos: a 0.1 mL.
        val cantMinR = if (medicamento.esSolido) redondearMedio(cantMin) else redondear(cantMin)
        val cantMaxR = if (medicamento.esSolido) redondearMedio(cantMax) else redondear(cantMax)
        val cantMaxDia = (cantMax * medicamento.maxDosisDia).let {
            if (medicamento.esSolido) redondearMedio(it) else redondear(it)
        }

        if (pesoKg <= 0.0) advertencias += "Ingrese un peso válido mayor a 0 kg."
        if (edadAnios <= 0.0) advertencias += "Ingrese una edad válida."
        if (medicamento.poblacion == Poblacion.PEDIATRICO && edadAnios > 12) {
            advertencias += "Este medicamento está marcado como pediátrico; verifique su uso en mayores de 12 años."
        }
        if (medicamento.poblacion == Poblacion.ADULTO && edadAnios > 0.0 && edadAnios < 12) {
            advertencias += "Este medicamento está marcado para adultos; verifique su uso en menores de 12 años."
        }
        if (medicamento.esSolido) {
            advertencias += "Presentación en ${medicamento.unidadDosis}: ajuste a la dosis comercial disponible (no siempre es posible fraccionar con exactitud)."
        }
        advertencias += "Dosis orientativa. Verifique la concentración exacta del producto (${medicamento.concentracionLabel})."
        advertencias += "No exceder ${medicamento.maxDosisDia} dosis en 24 horas."

        return ResultadoDosis(
            medicamento = medicamento.nombre,
            esSolido = medicamento.esSolido,
            unidad = medicamento.unidadDosis,
            cantidadMin = cantMinR,
            cantidadMax = cantMaxR,
            dosisMgMin = redondear(dosisMgMin),
            dosisMgMax = redondear(dosisMgMax),
            frecuencia = medicamento.frecuenciaLabel,
            maxDosisDia = medicamento.maxDosisDia,
            cantidadMaxDia = cantMaxDia,
            recomendaciones = medicamento.recomendaciones,
            advertencias = advertencias,
        )
    }

    private fun redondear(v: Double): Double = round(v * 10.0) / 10.0
    private fun redondearMedio(v: Double): Double = round(v * 2.0) / 2.0
}
