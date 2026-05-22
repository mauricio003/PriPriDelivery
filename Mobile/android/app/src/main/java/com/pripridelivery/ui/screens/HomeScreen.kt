package com.pripridelivery.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.pripridelivery.data.model.Restaurante
import com.pripridelivery.ui.theme.IFoodRed
import com.pripridelivery.viewmodel.AuthViewModel
import com.pripridelivery.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    onRestauranteClick: (String) -> Unit,
    onEnderecoClick: () -> Unit,
    onRestauranteGerenciarClick: () -> Unit,
    onPedidosClick: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val authState by authViewModel.uiState.collectAsState()
    val homeState by homeViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("PriPriDelivery", fontWeight = FontWeight.Bold)
                        Text(
                            "Olá, ${authState.usuario?.displayName ?: "Cliente"}!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onPedidosClick) {
                        Icon(Icons.Default.Receipt, contentDescription = "Meus Pedidos")
                    }
                    IconButton(onClick = onEnderecoClick) {
                        Icon(Icons.Default.LocationOn, contentDescription = "Endereços")
                    }
                    IconButton(onClick = {
                        authViewModel.logout()
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Sair")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onRestauranteGerenciarClick,
                containerColor = IFoodRed,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Store, contentDescription = "Gerenciar Restaurantes")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Barra de busca
            OutlinedTextField(
                value = homeState.busca,
                onValueChange = { homeViewModel.atualizarBusca(it) },
                placeholder = { Text("Buscar restaurante...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Categorias
            if (homeViewModel.categorias.isNotEmpty()) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = homeState.categoriaFiltro.isBlank(),
                            onClick = { homeViewModel.filtrarPorCategoria("") },
                            label = { Text("Todos") }
                        )
                    }
                    items(homeViewModel.categorias) { cat ->
                        FilterChip(
                            selected = homeState.categoriaFiltro == cat,
                            onClick = { homeViewModel.filtrarPorCategoria(cat) },
                            label = { Text(cat) }
                        )
                    }
                }
            }

            // Lista de restaurantes
            when {
                homeState.carregando -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = IFoodRed)
                    }
                }
                homeState.restaurantesFiltrados.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Nenhum restaurante encontrado", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(homeState.restaurantesFiltrados) { restaurante ->
                            RestauranteCard(restaurante = restaurante, onClick = { onRestauranteClick(restaurante.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RestauranteCard(restaurante: Restaurante, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Imagem
            if (restaurante.imagemUrl.isNotBlank()) {
                AsyncImage(
                    model = restaurante.imagemUrl,
                    contentDescription = restaurante.nome,
                    modifier = Modifier.fillMaxWidth().height(160.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Store, null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(restaurante.nome, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Icon(Icons.Default.Star, null, tint = IFoodRed, modifier = Modifier.size(16.dp))
                    Text("4.5", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 2.dp))
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (restaurante.categoria.isNotBlank()) {
                        Text(restaurante.categoria, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text("${restaurante.tempoEntregaNormal}-${restaurante.tempoEntregaRapida} min", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
