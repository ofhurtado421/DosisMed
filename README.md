# DosisMed — Dosificación clínica asistida

Aplicación **Kotlin Multiplatform (KMP)** con UI en **Compose Multiplatform**, pensada
como apoyo para profesionales de la salud. Funciona en **Android** y **Escritorio
(Windows / macOS / Linux)** compartiendo prácticamente todo el código.

Proyecto académico de: **Deimar Quiñonez** y **Oscar Fabian Hurtado Hueje**.

---

## ¿Qué hace?

1. **Pantalla de inicio (Splash)** con animación del nombre de la app.
2. **Inicio de sesión** con usuario y contraseña fijos (solo de presentación).
3. **Menú principal** con acceso a las cuatro secciones.
4. **Vademécum**: base de datos de medicamentos con subdivisiones por **tipo**,
   **forma farmacéutica** y **población** (pediátrico / adulto). Permite crear,
   editar y eliminar medicamentos, con búsqueda y filtros.
5. **Calculadora de dosis**: se elige un medicamento y se ingresan **peso** y **edad**;
   la app devuelve la **cantidad por toma (mL)**, la **frecuencia**, el **máximo diario**,
   las **recomendaciones** y las **advertencias**.
6. **Fórmulas / Tratamientos**: base de datos independiente donde se cargan
   padecimientos/síntomas y los medicamentos indicados, **buscable por lo que
   presenta el paciente**.
7. **Acerca de / Créditos**.

> Son **9 destinos de navegación** en total (más de los 5 requeridos).

### Credenciales de la demo
- Usuario: `admin`
- Contraseña: `1234`

---

## Cómo funciona el cálculo de dosis

En lugar de obligar a cargar una tabla por edad, cada medicamento guarda su rango
de **mg por kg de peso** y su **concentración**. La dosis se calcula así:

```
dosis_mg   = peso_kg × mg_por_kg
volumen_mL = dosis_mg / (concentración_mg / volumen_referencia_mL)
```

**Ejemplo (Acetaminofén jarabe 150 mg/5 mL, 10 mg/kg):**
un niño de 10 kg → 100 mg → 100 / 30 = **3.3 mL** por toma, cada 4–6 horas.
Esto reproduce la tabla pediátrica de referencia para *cualquier* peso, sin
necesidad de cargar fila por fila.

> Aviso: las dosis son orientativas y no sustituyen el criterio médico ni la
> información oficial del fabricante.

---

## Arquitectura

```
composeApp/
 └─ src/
    ├─ commonMain/                 ← TODO el código compartido (Android + Desktop)
    │  ├─ kotlin/com/dosismed/
    │  │  ├─ App.kt                 ← NavHost (navegación en la capa compartida)
    │  │  ├─ navigation/Routes.kt   ← rutas type-safe (kotlinx.serialization)
    │  │  ├─ di/AppContainer.kt     ← inyección de dependencias (service locator)
    │  │  ├─ domain/                ← modelos + DosisCalculator (lógica de negocio)
    │  │  ├─ data/                  ← repositorios + SQLDelight + driver (expect)
    │  │  └─ ui/                    ← pantallas Compose + tema
    │  └─ sqldelight/com/dosismed/db/  ← esquema SQL (.sq)
    ├─ androidMain/                 ← MainActivity, driver Android (actual), manifest
    └─ desktopMain/                 ← main.kt, driver JDBC (actual)
```

- **UI compartida:** Compose Multiplatform (Material 3).
- **Navegación:** librería oficial de JetBrains
  `org.jetbrains.androidx.navigation:navigation-compose`, gestionada en `commonMain`
  con rutas *type-safe*.
- **Persistencia:** SQLDelight (Android usa `android-driver`; Desktop usa
  `sqlite-driver` JDBC con archivo en `~/.dosismed/dosismed.db`).
- **Patrón `expect/actual`** para el driver de base de datos por plataforma.

---


## Versiones usadas

| Componente               | Versión        |
|--------------------------|----------------|
| Kotlin                   | 2.2.20         |
| Compose Multiplatform    | 1.11.0         |
| Android Gradle Plugin    | 8.7.3          |
| Gradle                   | 8.13           |
| navigation-compose (JB)  | 2.9.2          |
| SQLDelight               | 2.0.2          |
| kotlinx-coroutines       | 1.10.2         |
| kotlinx-serialization    | 1.8.0          |
| compileSdk / targetSdk   | 35             |
| minSdk                   | 24             |


