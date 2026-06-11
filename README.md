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

La app viene con **datos de ejemplo precargados** (Acetaminofén, Ibuprofeno,
Amoxicilina, Loratadina y tres fórmulas) para poder probarla de inmediato.

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

## Requisitos

- **JDK 17** o superior.
- **Android Studio** (versión reciente) con el **plugin de Kotlin Multiplatform**
  instalado, o **IntelliJ IDEA**.
- Para Android: un emulador o dispositivo con **Android 7.0 (API 24)** o superior.

---

## Cómo abrir y ejecutar

### Opción A — Abrir el proyecto incluido (recomendado)

1. Abre la carpeta `DosisMed` en **Android Studio** o **IntelliJ IDEA**
   (*File → Open*) y deja que sincronice Gradle.
2. **Ejecutar en Android:** selecciona la configuración del módulo `composeApp`
   y un emulador/dispositivo, y pulsa *Run*.
3. **Ejecutar en Escritorio** desde la terminal:
   ```bash
   ./gradlew :composeApp:run
   ```
4. **Generar instalador de escritorio** (.dmg / .msi / .deb):
   ```bash
   ./gradlew :composeApp:packageDistributionForCurrentOS
   ```

> El proyecto incluye el *Gradle Wrapper* (`./gradlew`), por lo que no necesitas
> instalar Gradle manualmente.

### Opción B — Plan B si hay problemas de sincronización o de versiones

Como las versiones de KMP/Compose cambian con frecuencia, si el *sync* falla por
un desajuste de versiones, la forma más segura es:

1. Generar un proyecto base nuevo con el **asistente oficial de Kotlin Multiplatform**
   en <https://kmp.jetbrains.com> (o el wizard del IDE), marcando **Android** y
   **Desktop** como objetivos. Esto garantiza versiones compatibles y el wrapper.
2. Copiar dentro de ese proyecto la carpeta `composeApp/src` de este repositorio
   (sobrescribiendo la generada).
3. Añadir al `composeApp/build.gradle.kts` las dependencias y plugins que aparecen
   en `gradle/libs.versions.toml` de este proyecto (navigation-compose, SQLDelight,
   kotlinx-serialization, kotlinx-coroutines), y el bloque `sqldelight { ... }`.

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

Todas las versiones están centralizadas en **`gradle/libs.versions.toml`**; si
necesitas actualizar alguna, edítala ahí.

---

## Nota sobre Voyager

Inicialmente se evaluó **Voyager** para la navegación, pero su última versión
estable es de hace más de un año y el último artefacto disponible está compilado
contra Kotlin 1.9, lo que genera riesgo de incompatibilidad con el toolchain actual
(Compose Multiplatform 1.11 / Kotlin 2.2). Por eso se usó la **navegación oficial de
JetBrains**, que está mantenida, vive en `commonMain` y cumple igual el requisito de
gestionar la navegación en la capa compartida.

---

## Créditos

- **Deimar Quiñonez**
- **Oscar Fabian Hurtado Hueje**

Proyecto académico — Kotlin Multiplatform (Android + Desktop).
