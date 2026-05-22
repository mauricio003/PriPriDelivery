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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pripridelivery.data.model.Produto
import com.pripridelivery.ui.theme.IFoodRed
import com.pripridelivery.util.FormatUtil
import com.pripridelivery.viewmodel.AuthViewModel
import com.pripridelivery.viewmodel.ProdutoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProdutosScreen(
    restauranteId: String,
    authViewModel: AuthViewModel,
    onVoltarClick: () -> Unit,
    produtoViewModel: ProdutoViewModel = hiltViewModel()
) {
    val state by produtoViewModel.uiState.collectAsState()

    LaunchedEffect(restauranteId) {
        produtoViewModel.carregarProdutos(restauranteId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Produtos") },
                navigationIcon = { IconButton(onClick = onVoltarClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") } }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { produtoViewModel.abrirFormulario() }, containerColor = IFoodRed) {
                Icon(Icons.Default.Add, "Novo", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            if (state.carregando) {
                CircularProgressIndicator(Modifier.align(Alignment.Center), color = IFoodRed)
            } else {
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.produtos) { produto ->
                        ProdutoGerenciarCard(
                            produto = produto,
                            onEditar = { produtoViewModel.abrirFormulario(produto) },
                            onExcluir = { produtoViewModel.excluir(restauranteId, produto.id) }
                        )
                    }
                }
            }

            if (state.mostrarFormulario) {
                ProdutoFormDialog(
                    produtoInicial = state.produtoEditando,
                    salvando = state.salvando,
                    onSalvar = { prod -> produtoViewModel.salvar(restauranteId, prod) },
                    onDismiss = { produtoViewModel.fecharFormulario() }
                )
            }
        }
    }
}

@Composable
fun ProdutoGerenciarCard(produto: Produto, onEditar: () -> Unit, onExcluir: () -> Unit) {
    Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(produto.nome, fontWeight = FontWeight.Bold)
            if (produto.descricao.isNotBlank()) Text(produto.descricao, style = MaterialTheme.typography.bodySmall, maxLines = 2)
            Spacer(Modifier.height(4.dp))
            Text(FormatUtil.formatarMoeda(produto.preco), color = IFoodRed, fontWeight = FontWeight.SemiBold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(produto.categoria, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                TextButton(onClick = onEditar) { Text("Editar", color = IFoodRed) }
                TextButton(onClick = onExcluir) { Text("Excluir", color = MaterialTheme.colorScheme.error) }
            }
        }
    }
}

@Composable
fun ProdutoFormDialog(produtoInicial: Produto?, salvando: Boolean, onSalvar: (Produto) -> Unit, onDismiss: () -> Unit) {
    var nome by remember { mutableStateOf(produtoInicial?.nome ?: "") }
    var descricao by remember { mutableStateOf(produtoInicial?.descricao ?: "") }
    var preco by remember { mutableStateOf(produtoInicial?.preco?.toString() ?: "") }
    var categoria by remember { mutableStateOf(produtoInicial?.categoria ?: "") }
    var imagemUrl by remember { mutableStateOf(produtoInicial?.imagemUrl ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (produtoInicial != null) "Editar Produto" else "Novo Produto") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(value = descricao, onValueChange = { descricao = it }, label = { Text("Descrição") }, modifier = Modifier.fillMaxWidth(), maxLines = 3)
                OutlinedTextField(value = preco, onValueChange = { preco = it }, label = { Text("Preço (R\$)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoria") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(value = imagemUrl, onValueChange = { imagemUrl = it }, label = { Text("URL da Imagem") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            }
        },
        confirmButton = {
            Button(onClick = {
                onSalvar(Produto(id = produtoInicial?.id ?: "", nome = nome, descricao = descricao,
                    preco = preco.toDoubleOrNull() ?: 0.0, categoria = categoria, imagemUrl = imagemUrl))
            }, enabled = !salvando && nome.isNotBlank(), colors = ButtonDefaults.buttonColors(containerColor = IFoodRed)) {
                if (salvando) CircularProgressIndicator(Modifier.size(16.dp)) else Text("Salvar")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
