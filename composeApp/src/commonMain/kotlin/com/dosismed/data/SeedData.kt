package com.dosismed.data

import com.dosismed.db.DosisMedDatabase
import com.dosismed.db.FormulaQueries
import com.dosismed.db.MedicamentoQueries

/** Carga datos de ejemplo la primera vez que se abre la app (para la demo). */
object SeedData {

    fun poblarSiVacio(db: DosisMedDatabase) {
        if (db.medicamentoQueries.count().executeAsOne() == 0L) {
            poblarMedicamentos(db.medicamentoQueries)
        }
        if (db.formulaQueries.count().executeAsOne() == 0L) {
            poblarFormulas(db.formulaQueries)
        }
    }

    // ---- Medicamentos ----

    private fun med(
        q: MedicamentoQueries,
        nombre: String, tipo: String, forma: String, poblacion: String,
        concentracionMg: Double, volumenReferenciaMl: Double,
        mgPorKgMin: Double, mgPorKgMax: Double,
        frecMin: Long, frecMax: Long, maxDia: Long,
        recomendaciones: String,
    ) = q.insert(
        nombre, tipo, forma, poblacion, concentracionMg, volumenReferenciaMl,
        mgPorKgMin, mgPorKgMax, frecMin, frecMax, maxDia, recomendaciones,
    )

    private fun poblarMedicamentos(q: MedicamentoQueries) {
        // --- Pediátricos (líquidos) ---
        med(q, "Acetaminofén jarabe", "ANTIPIRETICO", "JARABE", "PEDIATRICO",
            150.0, 5.0, 10.0, 15.0, 4L, 6L, 5L,
            "Antipirético y analgésico. Administrar con jeringa dosificadora. No combinar con otros productos que contengan acetaminofén.")
        med(q, "Ibuprofeno suspensión", "ANTIINFLAMATORIO", "SUSPENSION", "PEDIATRICO",
            100.0, 5.0, 5.0, 10.0, 6L, 8L, 4L,
            "Administrar con alimentos para reducir molestias gástricas. Evitar en deshidratación o problemas renales.")
        med(q, "Amoxicilina suspensión", "ANTIBIOTICO", "SUSPENSION", "PEDIATRICO",
            250.0, 5.0, 12.5, 25.0, 8L, 12L, 3L,
            "Antibiótico. Completar el tratamiento aunque mejoren los síntomas. Agitar antes de usar y refrigerar.")
        med(q, "Amoxicilina + ác. clavulánico susp.", "ANTIBIOTICO", "SUSPENSION", "PEDIATRICO",
            400.0, 5.0, 12.5, 22.5, 12L, 12L, 2L,
            "Antibiótico de amplio espectro. Tomar al inicio de las comidas. Refrigerar tras reconstituir.")
        med(q, "Cefalexina suspensión", "ANTIBIOTICO", "SUSPENSION", "PEDIATRICO",
            250.0, 5.0, 6.25, 12.5, 6L, 8L, 4L,
            "Antibiótico. Completar el tratamiento. Refrigerar la suspensión reconstituida.")
        med(q, "Azitromicina suspensión", "ANTIBIOTICO", "SUSPENSION", "PEDIATRICO",
            200.0, 5.0, 10.0, 10.0, 24L, 24L, 1L,
            "Una toma diaria por 3 a 5 días. Preferible con el estómago vacío. Agitar bien antes de usar.")
        med(q, "Cetirizina jarabe", "ANTIHISTAMINICO", "JARABE", "PEDIATRICO",
            5.0, 5.0, 0.2, 0.25, 24L, 24L, 1L,
            "Antialérgico de una toma diaria. Puede causar somnolencia leve.")
        med(q, "Loratadina jarabe", "ANTIHISTAMINICO", "JARABE", "AMBOS",
            5.0, 5.0, 0.1, 0.2, 24L, 24L, 1L,
            "Antialérgico de una toma diaria. Generalmente no produce somnolencia.")
        med(q, "Salbutamol jarabe", "OTRO", "JARABE", "PEDIATRICO",
            2.0, 5.0, 0.1, 0.2, 6L, 8L, 4L,
            "Broncodilatador. Puede causar temblor o taquicardia leve. Vigilar dificultad respiratoria.")
        med(q, "Prednisolona jarabe", "ANTIINFLAMATORIO", "JARABE", "PEDIATRICO",
            15.0, 5.0, 1.0, 2.0, 12L, 24L, 2L,
            "Corticoide. No suspender bruscamente en tratamientos prolongados. Administrar con alimentos.")
        med(q, "Ambroxol jarabe", "ANTITUSIVO", "JARABE", "PEDIATRICO",
            15.0, 5.0, 0.5, 0.75, 8L, 12L, 3L,
            "Mucolítico: favorece la expulsión de secreciones. Aumentar la ingesta de líquidos.")
        med(q, "Naproxeno suspensión", "ANTIINFLAMATORIO", "SUSPENSION", "PEDIATRICO",
            125.0, 5.0, 5.0, 7.0, 12L, 12L, 2L,
            "Antiinflamatorio. Administrar con alimentos. Evitar en problemas renales.")
        med(q, "Dipirona (metamizol) gotas", "ANALGESICO", "GOTAS", "AMBOS",
            500.0, 1.0, 10.0, 20.0, 6L, 8L, 4L,
            "Analgésico y antipirético potente. Suspender ante reacciones cutáneas. Cada mL equivale a 500 mg.")
        med(q, "Domperidona suspensión", "ANTIEMETICO", "SUSPENSION", "PEDIATRICO",
            5.0, 5.0, 0.25, 0.5, 8L, 8L, 3L,
            "Antiemético: ayuda con náuseas y vómito. Administrar antes de las comidas.")

        // --- Adultos (tableta / cápsula: cada 'unidad' equivale a la concentración indicada) ---
        med(q, "Acetaminofén tableta", "ANTIPIRETICO", "TABLETA", "ADULTO",
            500.0, 1.0, 10.0, 15.0, 6L, 8L, 4L,
            "Cada unidad equivale a 500 mg. No exceder 4 g al día.")
        med(q, "Ibuprofeno tableta", "ANTIINFLAMATORIO", "TABLETA", "ADULTO",
            400.0, 1.0, 5.0, 10.0, 8L, 8L, 3L,
            "Cada unidad equivale a 400 mg. Tomar con alimentos.")
        med(q, "Amoxicilina cápsula", "ANTIBIOTICO", "CAPSULA", "ADULTO",
            500.0, 1.0, 12.5, 15.0, 8L, 8L, 3L,
            "Cada unidad equivale a 500 mg. Completar el tratamiento.")
        med(q, "Naproxeno tableta", "ANTIINFLAMATORIO", "TABLETA", "ADULTO",
            250.0, 1.0, 4.0, 6.0, 12L, 12L, 2L,
            "Cada unidad equivale a 250 mg. Tomar con alimentos.")
        med(q, "Loratadina tableta", "ANTIHISTAMINICO", "TABLETA", "ADULTO",
            10.0, 1.0, 0.12, 0.15, 24L, 24L, 1L,
            "Cada unidad equivale a 10 mg. Una toma al día.")
        med(q, "Cetirizina tableta", "ANTIHISTAMINICO", "TABLETA", "ADULTO",
            10.0, 1.0, 0.12, 0.15, 24L, 24L, 1L,
            "Cada unidad equivale a 10 mg. Una toma al día.")
    }

    // ---- Fórmulas / Tratamientos ----

    private fun formula(
        q: FormulaQueries,
        padecimiento: String, sintomas: String, medicamentos: String, descripcion: String,
    ) = q.insert(padecimiento, sintomas, medicamentos, descripcion)

    private fun poblarFormulas(q: FormulaQueries) {
        formula(q, "Fiebre", "Temperatura mayor a 38°C, malestar general",
            "Acetaminofén; Ibuprofeno",
            "Manejo antitérmico según peso. Hidratación abundante. Acudir a urgencias si la fiebre supera 39.5°C o dura más de 3 días.")
        formula(q, "Gripe (influenza)", "Fiebre alta, dolor muscular, tos, congestión",
            "Acetaminofén; Loratadina",
            "Tratamiento sintomático. Reposo e hidratación. Los antibióticos no están indicados salvo sobreinfección bacteriana.")
        formula(q, "Resfriado común", "Estornudos, secreción nasal, congestión, tos leve",
            "Cetirizina; Acetaminofén",
            "Sintomático. Suele resolver en 7 a 10 días. Hidratación y reposo.")
        formula(q, "Otitis media aguda", "Dolor de oído, fiebre, irritabilidad",
            "Amoxicilina; Acetaminofén",
            "Antibioticoterapia y manejo del dolor. Reevaluar a las 48-72 horas si no hay mejoría.")
        formula(q, "Faringoamigdalitis", "Dolor de garganta intenso, fiebre, ganglios inflamados",
            "Amoxicilina; Ibuprofeno",
            "Si es de origen bacteriano (estreptococo), antibiótico por 10 días. Analgésico para el dolor.")
        formula(q, "Bronquitis aguda", "Tos productiva, malestar, a veces fiebre",
            "Ambroxol; Acetaminofén",
            "Generalmente de origen viral. Hidratación y mucolítico. Antibiótico solo si hay sobreinfección.")
        formula(q, "Bronquiolitis", "Tos, dificultad para respirar y sibilancias en lactantes",
            "Salbutamol",
            "Soporte e hidratación. Vigilar signos de dificultad respiratoria. Acudir a urgencias si empeora.")
        formula(q, "Crisis asmática leve", "Tos, sibilancias, opresión en el pecho",
            "Salbutamol; Prednisolona",
            "Broncodilatador y corticoide. Acudir a urgencias si no mejora o hay dificultad respiratoria.")
        formula(q, "Rinitis alérgica", "Estornudos, picor nasal y ocular, congestión",
            "Loratadina; Cetirizina",
            "Antihistamínico. Evitar los alérgenos identificados. Lavados nasales con solución salina.")
        formula(q, "Urticaria / alergia cutánea", "Ronchas, picor, enrojecimiento de la piel",
            "Cetirizina; Loratadina",
            "Antihistamínico. Si aparece dificultad respiratoria o hinchazón de labios/lengua, acudir a urgencias.")
        formula(q, "Gastroenteritis aguda", "Diarrea, vómito, dolor abdominal",
            "Domperidona; Acetaminofén",
            "La hidratación oral es lo principal. Antieméticos con precaución. Vigilar signos de deshidratación.")
        formula(q, "Cefalea (dolor de cabeza)", "Dolor de cabeza sin signos de alarma",
            "Acetaminofén; Ibuprofeno",
            "Analgésico. Descartar signos de alarma neurológicos (vómito en chorro, rigidez de cuello, déficit).")
        formula(q, "Dolor dental", "Dolor en una pieza dental, inflamación de la encía",
            "Ibuprofeno; Acetaminofén",
            "Analgésico/antiinflamatorio para el dolor. Remitir a odontología para tratamiento definitivo.")
        formula(q, "Infección urinaria", "Ardor al orinar, urgencia, dolor lumbar",
            "Cefalexina; Amoxicilina",
            "Antibiótico según urocultivo cuando sea posible. Aumentar la ingesta de líquidos.")
        formula(q, "Neumonía adquirida en comunidad", "Fiebre alta, tos, dificultad respiratoria",
            "Amoxicilina; Acetaminofén",
            "Antibioticoterapia. Valorar radiografía de tórax y signos de gravedad. Vigilar la respiración.")
        formula(q, "Sinusitis", "Dolor facial, congestión, secreción nasal espesa",
            "Amoxicilina; Ibuprofeno",
            "Manejo sintomático; antibiótico si los síntomas persisten más de 10 días o empeoran.")
    }
}
