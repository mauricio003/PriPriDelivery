package com.pripridelivery.ui.screens

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.pripridelivery.data.model.Produto
import com.pripridelivery.ui.theme.IFoodRed
import com.pripridelivery.util.FormatUtil
import com.pripridelivery.viewmodel.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComprarScreen(
    restauranteId: String,
    authViewModel: AuthViewModel,
    onVoltarClick: () -> Unit,
    onPagamentoClick: () -> Unit,
    produtoViewModel: ProdutoViewModel = hiltViewModel(),
    carrinhoViewModel: CarrinhoViewModel = hiltViewModel(),
    restauranteViewModel: RestauranteViewModel = hiltViewModel()
) {
    val authState by authViewModel.uiState.collectAsState()
    val produtoState by produtoViewModel.uiState.collectAsState()
    val carrinhoState by carrinhoViewModel.uiState.collectAsState()
    val restState by restauranteViewModel.uiState.collectAsState()
    val userId = authState.usuario?.uid ?: ""

    LaunchedEffect(restauranteId) {
        restauranteViewModel.carregarPorId(restauranteId)
        produtoViewModel.carregarProdutos(restauranteId, apenasDisponiveis = true)
    }

    LaunchedEffect(userId) {
        if (userId.isNotBlank()) carrinhoViewModel.carregarCarrinho(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(restState.restauranteAtual?.nome ?: "Restaurante") },
                navigationIcon = { IconButton(onClick = onVoltarClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") } },
                actions = {
                    BadgedBox(badge = {
                        if (carrinhoState.itens.isNotEmpty()) Badge { Text("${carrinhoState.itens.size}") }
                    }) {
                        IconButton(onClick = { carrinhoViewModel.abrirCarrinho() }) {
                            Icon(Icons.Default.ShoppingCart, "Carrinho")
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (produtoState.carregando) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = IFoodRed)
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Busca
                item {
                    OutlinedTextField(
                        value = produtoState.busca,
                        onValueChange = { produtoViewModel.atualizarBusca(it) },
                        placeholder = { Text("Buscar produto...") },
                        leadingIcon = { Icon(Icons.Default.Search, null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    Spacer(Modifier.height(8.dp))
                }

                // Produtos por categoria
                produtoViewModel.categorias.forEach { categoria ->
                    item {
                        Text(categoria, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
                    }
                    val produtosCategoria = produtoState.produtosFiltrados.filter { it.categoria == categoria }
                    items(produtosCategoria) { produto ->
                        ProdutoCompraCard(
                            produto = produto,
                            quantidade = produtoViewModel.getQuantidade(produto.id),
                            onMais = { produtoViewModel.ajustarQuantidade(produto.id, 1) },
                            onMenos = { produtoViewModel.ajustarQuantidade(produto.id, -1) },
                            onAdicionar = {
                                carrinhoViewModel.adicionarAoCarrinho(userId, produto.id, produtoViewModel.getQuantidade(produto.id), restauranteId)
                                produtoViewModel.resetarQuantidade(produto.id)
                            }
                        )
                    }
                }
            }
        }

        // Bottom Sheet do carrinho
        if (carrinhoState.carrinhoAberto) {
            ModalBottomSheet(onDismissRequest = { carrinhoViewModel.fecharCarrinho() }) {
                Column(Modifier.padding(16.dp)) {
                    Text("Carrinho", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(16.dp))

                    if (carrinhoState.itens.isEmpty()) {
                        Text("Seu carrinho está vazio", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        carrinhoState.itens.forEach { item ->
                            Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text(item.produto?.nome ?: "", fontWeight = FontWeight.Medium)
                                    Text(FormatUtil.formatarMoeda((item.produto?.preco ?: 0.0) * item.quantidade), color = IFoodRed)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = { carrinhoViewModel.diminuirQuantidade(userId, item.id, item.quantidade) }) {
                                        Icon(Icons.Default.Remove, "Menos", Modifier.size(18.dp))
                                    }
                                    Text("${item.quantidade}", fontWeight = FontWeight.Bold)
                                    IconButton(onClick = { carrinhoViewModel.aumentarQuantidade(userId, item.id, item.quantidade) }) {
                                        Icon(Icons.Default.Add, "Mais", Modifier.size(18.dp))
                                    }
                                    IconButton(onClick = { carrinhoViewModel.removerItem(userId, item.id) }) {
                                        Icon(Icons.Default.Delete, "Remover", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                            HorizontalDivider()
                        }

                        Spacer(Modifier.height(16.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Text(FormatUtil.formatarMoeda(carrinhoState.total), color = IFoodRed, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        }
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = { carrinhoViewModel.fecharCarrinho(); onPagamentoClick() },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = IFoodRed)
                        ) { Text("Finalizar Pedido", fontWeight = FontWeight.Bold) }
                    }
                    Spacer(Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun ProdutoCompraCard(produto: Produto, quantidade: Int, onMais: () -> Unit, onMenos: () -> Unit, onAdicionar: () -> Unit) {
    Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(12.dp)) {
            if (produto.imagemUrl.isNotBlank()) {
                AsyncImage(model = produto.imagemUrl, contentDescription = produto.nome,
                    modifier = Modifier.size(80.dp), contentScale = ContentScale.Crop)
                Spacer(Modifier.width(12.dp))
            }
            Column(Modifier.weight(1f)) {
                Text(produto.nome, fontWeight = FontWeight.SemiBold)
                if (produto.descricao.isNotBlank()) Text(produto.descricao, style = MaterialTheme.typography.bodySmall, maxLines = 2, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(4.dp))
                Text(FormatUtil.formatarMoeda(produto.preco), color = IFoodRed, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onMenos, Modifier.size(28.dp)) { Icon(Icons.Default.Remove, "Menos", Modifier.size(16.dp)) }
                    Text("$quantidade", Modifier.padding(horizontal = 8.dp), fontWeight = FontWeight.Bold)
                    IconButton(onClick = onMais, Modifier.size(28.dp)) { Icon(Icons.Default.Add, "Mais", Modifier.size(16.dp)) }
                    Spacer(Modifier.weight(1f))
                    Button(onClick = onAdicionar, shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.buttonColors(containerColor = IFoodRed),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)) {
                        Icon(Icons.Default.ShoppingCart, null, Modifier.size(14.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Adicionar", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}
