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
import com.pripridelivery.data.model.Restaurante
import com.pripridelivery.ui.theme.IFoodRed
import com.pripridelivery.viewmodel.AuthViewModel
import com.pripridelivery.viewmodel.RestauranteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestauranteScreen(
    authViewModel: AuthViewModel,
    onProdutosClick: (String) -> Unit,
    onVoltarClick: () -> Unit,
    restauranteViewModel: RestauranteViewModel = hiltViewModel()
) {
    val authState by authViewModel.uiState.collectAsState()
    val state by restauranteViewModel.uiState.collectAsState()
    val userId = authState.usuario?.uid ?: ""

    LaunchedEffect(userId) {
        if (userId.isNotBlank()) restauranteViewModel.carregarPorUsuario(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Restaurantes") },
                navigationIcon = { IconButton(onClick = onVoltarClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") } }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { restauranteViewModel.abrirFormulario() }, containerColor = IFoodRed) {
                Icon(Icons.Default.Add, "Novo", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            if (state.carregando) {
                CircularProgressIndicator(Modifier.align(Alignment.Center), color = IFoodRed)
            } else if (state.restaurantes.isEmpty() && !state.mostrarFormulario) {
                Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Store, null, Modifier.size(64.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(8.dp))
                    Text("Nenhum restaurante cadastrado", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.restaurantes) { rest ->
                        RestauranteGerenciarCard(
                            restaurante = rest,
                            onProdutos = { onProdutosClick(rest.id) },
                            onEditar = { restauranteViewModel.abrirFormulario(rest) },
                            onExcluir = { restauranteViewModel.excluir(userId, rest.id) }
                        )
                    }
                }
            }

            if (state.mostrarFormulario) {
                RestauranteFormDialog(
                    restauranteInicial = state.restauranteAtual,
                    salvando = state.salvando,
                    onSalvar = { rest -> restauranteViewModel.salvar(userId, rest) },
                    onDismiss = { restauranteViewModel.fecharFormulario() }
                )
            }
        }
    }
}

@Composable
fun RestauranteGerenciarCard(restaurante: Restaurante, onProdutos: () -> Unit, onEditar: () -> Unit, onExcluir: () -> Unit) {
    Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(restaurante.nome, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            if (restaurante.categoria.isNotBlank()) {
                Text(restaurante.categoria, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (restaurante.descricao.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(restaurante.descricao, style = MaterialTheme.typography.bodySmall, maxLines = 2)
            }
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = onProdutos) { Icon(Icons.Default.Fastfood, null, Modifier.size(16.dp)); Spacer(Modifier.width(4.dp)); Text("Produtos") }
                TextButton(onClick = onEditar) { Text("Editar", color = IFoodRed) }
                TextButton(onClick = onExcluir) { Text("Excluir", color = MaterialTheme.colorScheme.error) }
            }
        }
    }
}

@Composable
fun RestauranteFormDialog(restauranteInicial: Restaurante?, salvando: Boolean, onSalvar: (Restaurante) -> Unit, onDismiss: () -> Unit) {
    var nome by remember { mutableStateOf(restauranteInicial?.nome ?: "") }
    var descricao by remember { mutableStateOf(restauranteInicial?.descricao ?: "") }
    var categoria by remember { mutableStateOf(restauranteInicial?.categoria ?: "") }
    var imagemUrl by remember { mutableStateOf(restauranteInicial?.imagemUrl ?: "") }
    var horarioAbertura by remember { mutableStateOf(restauranteInicial?.horarioAbertura ?: "10:00") }
    var horarioFechamento by remember { mutableStateOf(restauranteInicial?.horarioFechamento ?: "23:00") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (restauranteInicial != null) "Editar Restaurante" else "Novo Restaurante") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(value = descricao, onValueChange = { descricao = it }, label = { Text("Descrição") }, modifier = Modifier.fillMaxWidth(), maxLines = 3)
                OutlinedTextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoria") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(value = imagemUrl, onValueChange = { imagemUrl = it }, label = { Text("URL da Imagem") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = horarioAbertura, onValueChange = { horarioAbertura = it }, label = { Text("Abertura") }, modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = horarioFechamento, onValueChange = { horarioFechamento = it }, label = { Text("Fechamento") }, modifier = Modifier.weight(1f), singleLine = true)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onSalvar(Restaurante(id = restauranteInicial?.id ?: "", nome = nome, descricao = descricao, categoria = categoria,
                    imagemUrl = imagemUrl, horarioAbertura = horarioAbertura, horarioFechamento = horarioFechamento))
            }, enabled = !salvando && nome.isNotBlank(), colors = ButtonDefaults.buttonColors(containerColor = IFoodRed)) {
                if (salvando) CircularProgressIndicator(Modifier.size(16.dp)) else Text("Salvar")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
