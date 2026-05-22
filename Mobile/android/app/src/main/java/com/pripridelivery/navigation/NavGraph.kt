package com.pripridelivery.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pripridelivery.ui.screens.*
import com.pripridelivery.viewmodel.AuthViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.uiState.collectAsState()

    val startDestination = if (authState.carregando) "splash" else if (authState.estaAutenticado) "home" else "login"

    NavHost(navController = navController, startDestination = startDestination) {

        composable("splash") { SplashScreen() }

        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = { navController.navigate("home") { popUpTo("login") { inclusive = true } } },
                onCadastroClick = { navController.navigate("cadastro") }
            )
        }

        composable("cadastro") {
            CadastroScreen(
                authViewModel = authViewModel,
                onCadastroSuccess = { navController.navigate("home") { popUpTo("cadastro") { inclusive = true } } },
                onVoltarClick = { navController.popBackStack() }
            )
        }

        composable("home") {
            HomeScreen(
                authViewModel = authViewModel,
                onRestauranteClick = { id -> navController.navigate("comprar/$id") },
                onEnderecoClick = { navController.navigate("endereco") },
                onRestauranteGerenciarClick = { navController.navigate("restaurante") },
                onPedidosClick = { navController.navigate("pedidos") }
            )
        }

        composable("endereco") {
            EnderecoScreen(
                authViewModel = authViewModel,
                onVoltarClick = { navController.popBackStack() }
            )
        }

        composable("restaurante") {
            RestauranteScreen(
                authViewModel = authViewModel,
                onProdutosClick = { id -> navController.navigate("produtos/$id") },
                onVoltarClick = { navController.popBackStack() }
            )
        }

        composable(
            "produtos/{restauranteId}",
            arguments = listOf(navArgument("restauranteId") { type = NavType.StringType })
        ) { backStackEntry ->
            val restauranteId = backStackEntry.arguments?.getString("restauranteId") ?: ""
            ProdutosScreen(
                restauranteId = restauranteId,
                authViewModel = authViewModel,
                onVoltarClick = { navController.popBackStack() }
            )
        }

        composable(
            "comprar/{restauranteId}",
            arguments = listOf(navArgument("restauranteId") { type = NavType.StringType })
        ) { backStackEntry ->
            val restauranteId = backStackEntry.arguments?.getString("restauranteId") ?: ""
            ComprarScreen(
                restauranteId = restauranteId,
                authViewModel = authViewModel,
                onVoltarClick = { navController.popBackStack() },
                onPagamentoClick = { navController.navigate("pagamento") }
            )
        }

        composable("pagamento") {
            PagamentoScreen(
                authViewModel = authViewModel,
                onVoltarClick = { navController.popBackStack() },
                onPedidoConfirmado = { pedidoId, codigo ->
                    navController.navigate("acompanhamento/$pedidoId/$codigo") {
                        popUpTo("pagamento") { inclusive = true }
                    }
                }
            )
        }

        composable(
            "acompanhamento/{pedidoId}/{codigoVerificacao}",
            arguments = listOf(
                navArgument("pedidoId") { type = NavType.StringType },
                navArgument("codigoVerificacao") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val pedidoId = backStackEntry.arguments?.getString("pedidoId") ?: ""
            val codigoVerificacao = backStackEntry.arguments?.getString("codigoVerificacao") ?: ""
            AcompanhamentoScreen(
                pedidoId = pedidoId,
                codigoVerificacao = codigoVerificacao,
                onVoltarClick = { navController.navigate("home") { popUpTo(0) } }
            )
        }

        composable("pedidos") {
            MeusPedidosScreen(
                authViewModel = authViewModel,
                onVoltarClick = { navController.popBackStack() },
                onPedidoClick = { pedidoId ->
                    navController.navigate("acompanhamento/$pedidoId/none")
                }
            )
        }
    }
}
