package com.pripridelivery.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pripridelivery.data.model.Pedido
import com.pripridelivery.ui.theme.GreenSuccess
import com.pripridelivery.ui.theme.IFoodRed
import com.pripridelivery.util.FormatUtil
import com.pripridelivery.viewmodel.AuthViewModel
import com.pripridelivery.viewmodel.PedidoViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeusPedidosScreen(
    authViewModel: AuthViewModel,
    onVoltarClick: () -> Unit,
    onPedidoClick: (String) -> Unit,
    pedidoViewModel: PedidoViewModel = hiltViewModel()
) {
    val authState by authViewModel.uiState.collectAsState()
    val state by pedidoViewModel.uiState.collectAsState()
    val userId = authState.usuario?.uid ?: ""

    LaunchedEffect(userId) {
        if (userId.isNotBlank()) pedidoViewModel.carregarPedidos(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Pedidos") },
                navigationIcon = {
                    IconButton(onClick = onVoltarClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when {
                state.carregando -> {
                    CircularProgressIndicator(
                        Modifier.align(Alignment.Center),
                        color = IFoodRed
                    )
                }
                state.pedidos.isEmpty() -> {
                    Column(
                        Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.ShoppingBag, null,
                            Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Você ainda não fez nenhum pedido.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(16.dp))
                        TextButton(onClick = onVoltarClick) {
                            Text("Ir para a página inicial", color = IFoodRed)
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.pedidos) { pedido ->
                            PedidoCard(
                                pedido = pedido,
                                onClick = { onPedidoClick(pedido.pedidoId) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PedidoCard(pedido: Pedido, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            // Cabeçalho
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Pedido #${pedido.pedidoId}",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Schedule, null,
                            Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            formatarData(pedido.createdAt),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            pedido.status.ifBlank { "Em andamento" },
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = GreenSuccess.copy(alpha = 0.1f)
                    )
                )
            }

            HorizontalDivider(Modifier.padding(vertical = 8.dp))

            // Itens
            pedido.itens.forEach { item ->
                Text(
                    "${item.quantidade}x ${item.nome}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider(Modifier.padding(vertical = 8.dp))

            // Total
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Total: ${FormatUtil.formatarMoeda(pedido.total)}",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Ver Acompanhamento",
                    color = IFoodRed,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

private fun formatarData(dataStr: String): String {
    return try {
        val dt = LocalDateTime.parse(dataStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm"))
    } catch (_: Exception) {
        dataStr
    }
}
