package com.pripridelivery.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pripridelivery.ui.theme.IFoodRed
import com.pripridelivery.util.FormatUtil
import com.pripridelivery.viewmodel.AuthViewModel
import com.pripridelivery.viewmodel.DadosCartao
import com.pripridelivery.viewmodel.PagamentoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagamentoScreen(
    authViewModel: AuthViewModel,
    onVoltarClick: () -> Unit,
    onPedidoConfirmado: (String, String) -> Unit,
    pagamentoViewModel: PagamentoViewModel = hiltViewModel()
) {
    val authState by authViewModel.uiState.collectAsState()
    val state by pagamentoViewModel.uiState.collectAsState()
    val userId = authState.usuario?.uid ?: ""

    // Detecta sucesso do pagamento
    LaunchedEffect(state.sucesso) {
        if (state.sucesso && state.pedidoId != null && state.codigoVerificacao != null) {
            onPedidoConfirmado(state.pedidoId!!, state.codigoVerificacao!!)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pagamento") },
                navigationIcon = {
                    IconButton(onClick = onVoltarClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Resumo do pedido
            Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Resumo do Pedido", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    state.itens.forEach { item ->
                        Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Text("${item.quantidade}x ${item.produto?.nome ?: ""}", Modifier.weight(1f))
                            Text(FormatUtil.formatarMoeda((item.produto?.preco ?: 0.0) * item.quantidade))
                        }
                    }
                    HorizontalDivider(Modifier.padding(vertical = 8.dp))
                    Row(Modifier.fillMaxWidth()) {
                        Text("Total", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        Text(FormatUtil.formatarMoeda(state.total), color = IFoodRed, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Tipo de entrega
            Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Tipo de Entrega", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = state.tipoEntrega == "entrega",
                            onClick = { pagamentoViewModel.selecionarTipoEntrega("entrega") },
                            label = { Text("Entrega") },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = state.tipoEntrega == "retirada",
                            onClick = { pagamentoViewModel.selecionarTipoEntrega("retirada") },
                            label = { Text("Retirada") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Endereço
            Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Endereço de Entrega", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    if (state.enderecos.isEmpty()) {
                        Text("Nenhum endereço cadastrado", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        state.enderecos.forEach { endereco ->
                            Row(
                                Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = state.enderecoSelecionado?.id == endereco.id,
                                    onClick = { pagamentoViewModel.selecionarEndereco(endereco) }
                                )
                                Column(Modifier.padding(start = 8.dp)) {
                                    Text("${endereco.logradouro}, ${endereco.numero}", fontWeight = FontWeight.Medium)
                                    Text("${endereco.bairro} - ${endereco.cidade}/${endereco.estado}",
                                        style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Forma de pagamento
            Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Forma de Pagamento", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = state.formaPagamento == "cartao",
                            onClick = { pagamentoViewModel.selecionarFormaPagamento("cartao") },
                            label = { Text("Cartão") },
                            leadingIcon = { Icon(Icons.Default.CreditCard, null, Modifier.size(16.dp)) },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = state.formaPagamento == "pix",
                            onClick = { pagamentoViewModel.selecionarFormaPagamento("pix") },
                            label = { Text("PIX") },
                            leadingIcon = { Icon(Icons.Default.QrCode, null, Modifier.size(16.dp)) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (state.formaPagamento == "cartao") {
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(
                            value = state.dadosCartao.numero, onValueChange = { pagamentoViewModel.atualizarCartao(state.dadosCartao.copy(numero = it)) },
                            label = { Text("Número do Cartão") }, modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true
                        )
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = state.dadosCartao.nome, onValueChange = { pagamentoViewModel.atualizarCartao(state.dadosCartao.copy(nome = it)) },
                            label = { Text("Nome no Cartão") }, modifier = Modifier.fillMaxWidth(), singleLine = true
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = state.dadosCartao.validade, onValueChange = { pagamentoViewModel.atualizarCartao(state.dadosCartao.copy(validade = it)) },
                                label = { Text("Validade") }, modifier = Modifier.weight(1f), singleLine = true, placeholder = { Text("MM/AA") }
                            )
                            OutlinedTextField(
                                value = state.dadosCartao.cvv, onValueChange = { pagamentoViewModel.atualizarCartao(state.dadosCartao.copy(cvv = it)) },
                                label = { Text("CVV") }, modifier = Modifier.weight(1f), singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }
                    }

                    if (state.formaPagamento == "pix") {
                        Spacer(Modifier.height(16.dp))
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.QrCode, null, Modifier.size(120.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(Modifier.height(8.dp))
                                Text("Escaneie o QR Code com seu app de pagamento", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            // Erro
            state.erro?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(24.dp))

            // Botão finalizar
            Button(
                onClick = { pagamentoViewModel.processarPagamento(userId) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = IFoodRed),
                enabled = !state.carregando && state.formaPagamento.isNotBlank()
            ) {
                if (state.carregando) {
                    CircularProgressIndicator(Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Finalizar Pedido", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}
