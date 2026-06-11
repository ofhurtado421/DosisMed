package com.dosismed.domain.model

/** Tipo o categoría terapéutica del medicamento (subdivisión del vademécum). */
enum class TipoMedicamento(val label: String) {
    ANALGESICO("Analgésico"),
    ANTIPIRETICO("Antipirético"),
    ANTIBIOTICO("Antibiótico"),
    ANTIINFLAMATORIO("Antiinflamatorio"),
    ANTIHISTAMINICO("Antihistamínico"),
    ANTITUSIVO("Antitusivo"),
    ANTIEMETICO("Antiemético"),
    OTRO("Otro");

    companion object {
        fun fromName(name: String): TipoMedicamento = entries.firstOrNull { it.name == name } ?: OTRO
    }
}

/** Forma farmacéutica (subdivisión por presentación). */
enum class FormaFarmaceutica(val label: String) {
    JARABE("Jarabe"),
    SUSPENSION("Suspensión"),
    GOTAS("Gotas"),
    SOLUCION("Solución"),
    TABLETA("Tableta"),
    CAPSULA("Cápsula");

    companion object {
        fun fromName(name: String): FormaFarmaceutica = entries.firstOrNull { it.name == name } ?: JARABE
    }
}

/** Población objetivo (subdivisión pediátrico / adulto). */
enum class Poblacion(val label: String) {
    PEDIATRICO("Pediátrico"),
    ADULTO("Adulto"),
    AMBOS("Pediátrico y adulto");

    companion object {
        fun fromName(name: String): Poblacion = entries.firstOrNull { it.name == name } ?: AMBOS
    }
}
