package com.dosismed

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.dosismed.navigation.Route
import com.dosismed.ui.acerca.AcercaScreen
import com.dosismed.ui.calculadora.CalculadoraScreen
import com.dosismed.ui.formulas.FormFormulaScreen
import com.dosismed.ui.formulas.FormulasScreen
import com.dosismed.ui.login.LoginScreen
import com.dosismed.ui.medicamentos.FormMedicamentoScreen
import com.dosismed.ui.medicamentos.ListaMedicamentosScreen
import com.dosismed.ui.menu.MenuScreen
import com.dosismed.ui.splash.SplashScreen
import com.dosismed.ui.theme.DosisMedTheme

@Composable
fun App() {
    DosisMedTheme {
        val nav = rememberNavController()

        NavHost(navController = nav, startDestination = Route.Splash) {

            composable<Route.Splash> {
                SplashScreen(onTimeout = {
                    nav.navigate(Route.Login) {
                        popUpTo(Route.Splash) { inclusive = true }
                    }
                })
            }

            composable<Route.Login> {
                LoginScreen(onLoginOk = {
                    nav.navigate(Route.Menu) {
                        popUpTo(Route.Login) { inclusive = true }
                    }
                })
            }

            composable<Route.Menu> {
                MenuScreen(
                    onMedicamentos = { nav.navigate(Route.Medicamentos) },
                    onCalculadora = { nav.navigate(Route.Calculadora) },
                    onFormulas = { nav.navigate(Route.Formulas) },
                    onAcerca = { nav.navigate(Route.Acerca) },
                    onLogout = {
                        nav.navigate(Route.Login) {
                            popUpTo(Route.Menu) { inclusive = true }
                        }
                    },
                )
            }

            composable<Route.Medicamentos> {
                ListaMedicamentosScreen(
                    onBack = { nav.popBackStack() },
                    onNuevo = { nav.navigate(Route.FormMedicamento(0L)) },
                    onEditar = { id -> nav.navigate(Route.FormMedicamento(id)) },
                )
            }

            composable<Route.FormMedicamento> { backStackEntry ->
                val args = backStackEntry.toRoute<Route.FormMedicamento>()
                FormMedicamentoScreen(
                    medicamentoId = args.id,
                    onBack = { nav.popBackStack() },
                )
            }

            composable<Route.Calculadora> {
                CalculadoraScreen(onBack = { nav.popBackStack() })
            }

            composable<Route.Formulas> {
                FormulasScreen(
                    onBack = { nav.popBackStack() },
                    onNueva = { nav.navigate(Route.FormFormula(0L)) },
                    onEditar = { id -> nav.navigate(Route.FormFormula(id)) },
                )
            }

            composable<Route.FormFormula> { backStackEntry ->
                val args = backStackEntry.toRoute<Route.FormFormula>()
                FormFormulaScreen(
                    formulaId = args.id,
                    onBack = { nav.popBackStack() },
                )
            }

            composable<Route.Acerca> {
                AcercaScreen(onBack = { nav.popBackStack() })
            }
        }
    }
}
