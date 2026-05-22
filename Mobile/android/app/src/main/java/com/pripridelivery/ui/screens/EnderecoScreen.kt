package com.pripridelivery.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import com.pripridelivery.data.model.Endereco
import com.pripridelivery.ui.theme.IFoodRed
import com.pripridelivery.ui.theme.GreenSuccess
import com.pripridelivery.viewmodel.AuthViewModel
import com.pripridelivery.viewmodel.EnderecoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnderecoScreen(
    authViewModel: AuthViewModel,
    onVoltarClick: () -> Unit,
    enderecoViewModel: EnderecoViewModel = hiltViewModel()
) {
    val authState by authViewModel.uiState.collectAsState()
    val state by enderecoViewModel.uiState.collectAsState()
    val userId = authState.usuario?.uid ?: ""

    LaunchedEffect(userId) {
        if (userId.isNotBlank()) enderecoViewModel.carregarEnderecos(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Endereços") },
                navigationIcon = { IconButton(onClick = onVoltarClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") } }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { enderecoViewModel.iniciarEdicao(null) },
                containerColor = IFoodRed
            ) { Icon(Icons.Default.Add, "Novo endereço", tint = MaterialTheme.colorScheme.onPrimary) }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (state.carregando) {
                CircularProgressIndicator(Modifier.align(Alignment.Center), color = IFoodRed)
            } else if (state.enderecos.isEmpty() && !state.mostrarFormulario) {
                Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.LocationOff, null, Modifier.size(64.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(8.dp))
                    Text("Nenhum endereço cadastrado", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.enderecos) { endereco ->
                        EnderecoCard(
                            endereco = endereco,
                            onEditar = { enderecoViewModel.iniciarEdicao(endereco) },
                            onExcluir = { enderecoViewModel.excluirEndereco(userId, endereco.id) }
                        )
                    }
                }
            }

            // Modal do formulário
            if (state.mostrarFormulario) {
                EnderecoFormDialog(
                    enderecoInicial = state.enderecoEditando,
                    salvando = state.salvando,
                    onSalvar = { end -> enderecoViewModel.salvarEndereco(userId, end) },
                    onDismiss = { enderecoViewModel.cancelarFormulario() }
                )
            }
        }
    }
}

@Composable
fun EnderecoCard(endereco: Endereco, onEditar: () -> Unit, onExcluir: () -> Unit) {
    Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, null, tint = IFoodRed)
                Spacer(Modifier.width(8.dp))
                Text("${endereco.logradouro}, ${endereco.numero}", fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                if (endereco.principal) {
                    AssistChip(onClick = {}, label = { Text("Principal", style = MaterialTheme.typography.labelSmall) },
                        colors = AssistChipDefaults.assistChipColors(containerColor = GreenSuccess.copy(alpha = 0.1f)))
                }
            }
            if (endereco.complemento.isNotBlank()) Text(endereco.complemento, style = MaterialTheme.typography.bodySmall)
            Text("${endereco.bairro} - ${endereco.cidade}/${endereco.estado}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("CEP: ${endereco.cep}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onEditar) { Text("Editar", color = IFoodRed) }
                TextButton(onClick = onExcluir) { Text("Excluir", color = MaterialTheme.colorScheme.error) }
            }
        }
    }
}

@Composable
fun EnderecoFormDialog(enderecoInicial: Endereco?, salvando: Boolean, onSalvar: (Endereco) -> Unit, onDismiss: () -> Unit) {
    var cep by remember { mutableStateOf(enderecoInicial?.cep ?: "") }
    var logradouro by remember { mutableStateOf(enderecoInicial?.logradouro ?: "") }
    var numero by remember { mutableStateOf(enderecoInicial?.numero ?: "") }
    var complemento by remember { mutableStateOf(enderecoInicial?.complemento ?: "") }
    var bairro by remember { mutableStateOf(enderecoInicial?.bairro ?: "") }
    var cidade by remember { mutableStateOf(enderecoInicial?.cidade ?: "") }
    var estado by remember { mutableStateOf(enderecoInicial?.estado ?: "") }
    var principal by remember { mutableStateOf(enderecoInicial?.principal ?: false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (enderecoInicial != null) "Editar Endereço" else "Novo Endereço") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = cep, onValueChange = { cep = it }, label = { Text("CEP") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(value = logradouro, onValueChange = { logradouro = it }, label = { Text("Logradouro") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = numero, onValueChange = { numero = it }, label = { Text("Nº") }, modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = complemento, onValueChange = { complemento = it }, label = { Text("Compl.") }, modifier = Modifier.weight(1f), singleLine = true)
                }
                OutlinedTextField(value = bairro, onValueChange = { bairro = it }, label = { Text("Bairro") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = cidade, onValueChange = { cidade = it }, label = { Text("Cidade") }, modifier = Modifier.weight(2f), singleLine = true)
                    OutlinedTextField(value = estado, onValueChange = { estado = it }, label = { Text("UF") }, modifier = Modifier.weight(1f), singleLine = true)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = principal, onCheckedChange = { principal = it })
                    Text("Endereço principal")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onSalvar(Endereco(id = enderecoInicial?.id ?: "", cep = cep, logradouro = logradouro, numero = numero,
                    complemento = complemento, bairro = bairro, cidade = cidade, estado = estado, principal = principal))
            }, enabled = !salvando, colors = ButtonDefaults.buttonColors(containerColor = IFoodRed)) {
                if (salvando) CircularProgressIndicator(Modifier.size(16.dp), color = MaterialTheme.colorScheme.onPrimary)
                else Text("Salvar")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
