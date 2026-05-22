package com.pripridelivery.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.pripridelivery.ui.theme.*
import com.pripridelivery.viewmodel.PedidoViewModel

data class StatusInfo(val texto: String, val icone: ImageVector, val cor: androidx.compose.ui.graphics.Color)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcompanhamentoScreen(
    pedidoId: String,
    codigoVerificacao: String,
    onVoltarClick: () -> Unit,
    pedidoViewModel: PedidoViewModel = hiltViewModel()
) {
    val state by pedidoViewModel.uiState.collectAsState()

    val statusList = remember {
        listOf(
            StatusInfo("O restaurante aceitou o pedido", Icons.Default.Store, BlueInfo),
            StatusInfo("Pedido sendo preparado", Icons.Default.Restaurant, YellowWarning),
            StatusInfo("Encontrando motorista parceiro", Icons.Default.Search, PurpleAccent),
            StatusInfo("Seu motorista está indo até você", Icons.Default.LocalShipping, OrangeAccent),
            StatusInfo("Seu pedido chegou", Icons.Default.CheckCircle, GreenSuccess)
        )
    }

    LaunchedEffect(pedidoId) {
        pedidoViewModel.iniciarAcompanhamento(pedidoId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Acompanhamento do Pedido") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(24.dp)) {
                    statusList.forEachIndexed { index, status ->
                        val isAtivo = index <= state.statusAtual
                        val alpha = if (isAtivo) 1f else 0.4f

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Ícone circular
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isAtivo) status.cor.copy(alpha = 0.15f)
                                        else MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    status.icone, null,
                                    tint = if (isAtivo) status.cor else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Spacer(Modifier.width(16.dp))

                            Text(
                                status.texto,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (isAtivo) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isAtivo) status.cor else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha),
                                modifier = Modifier.weight(1f)
                            )

                            if (isAtivo) {
                                Icon(Icons.Default.Check, null, tint = status.cor, modifier = Modifier.size(20.dp))
                            }
                        }

                        // Linha conectora
                        if (index < statusList.size - 1) {
                            Box(
                                Modifier
                                    .padding(start = 21.dp)
                                    .width(2.dp)
                                    .height(16.dp)
                                    .background(
                                        if (index < state.statusAtual) status.cor.copy(alpha = 0.3f)
                                        else MaterialTheme.colorScheme.outlineVariant
                                    )
                            )
                        }
                    }
                }
            }

            // Mensagem de sucesso
            if (state.pedidoConcluido) {
                Spacer(Modifier.height(24.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = GreenLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CheckCircle, null, tint = GreenSuccess, modifier = Modifier.size(48.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("Pedido entregue com sucesso!", color = GreenSuccess, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Obrigado por comprar com a PriPriDelivery!", style = MaterialTheme.typography.bodySmall)
                    }
                }

                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = onVoltarClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IFoodRed)
                ) { Text("Voltar ao início") }
            }
        }

        // Modal de verificação
        if (state.mostrarModalVerificacao) {
            Dialog(onDismissRequest = {}) {
                Card(shape = RoundedCornerShape(24.dp)) {
                    Column(
                        Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            Modifier.size(64.dp).clip(CircleShape)
                                .background(IFoodRed.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.LocalShipping, null, tint = IFoodRed, modifier = Modifier.size(32.dp))
                        }

                        Spacer(Modifier.height(16.dp))
                        Text("Seu pedido chegou!", fontWeight = FontWeight.Bold, fontSize = 22.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Insira o código de 6 dígitos enviado para o seu e-mail",
                            textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(Modifier.height(24.dp))
                        OutlinedTextField(
                            value = state.codigoInserido,
                            onValueChange = { if (it.length <= 6) pedidoViewModel.atualizarCodigoInserido(it) },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center, fontSize = 28.sp, fontWeight = FontWeight.Bold, letterSpacing = 8.sp),
                            placeholder = { Text("000000", Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true
                        )

                        if (state.erroVerificacao.isNotBlank()) {
                            Spacer(Modifier.height(8.dp))
                            Text(state.erroVerificacao, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                        }

                        Spacer(Modifier.height(24.dp))
                        Button(
                            onClick = { pedidoViewModel.verificarCodigo(codigoVerificacao) },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = IFoodRed),
                            enabled = state.codigoInserido.length == 6
                        ) { Text("Confirmar Entrega", fontWeight = FontWeight.Bold) }
                    }
                }
            }
        }
    }
}
