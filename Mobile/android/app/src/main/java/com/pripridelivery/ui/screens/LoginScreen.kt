package com.pripridelivery.ui.screens

import android.app.Activity
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pripridelivery.ui.theme.IFoodRed
import com.pripridelivery.ui.theme.IFoodRedDark
import com.pripridelivery.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onCadastroClick: () -> Unit
) {
    val uiState by authViewModel.uiState.collectAsState()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val webClientId = "618272619464-mahl9qo2vdau3h1174s14ke9nqc1qqil.apps.googleusercontent.com"

    var metodoLogin by remember { mutableStateOf("email") }   // "email" ou "telefone"
    var email by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var codigoOtp by remember { mutableStateOf("") }
    var etapaOtp by remember { mutableIntStateOf(0) }  // 0 = entrada, 1 = código

    // Detecta login bem-sucedido
    LaunchedEffect(uiState.estaAutenticado) {
        if (uiState.estaAutenticado) onLoginSuccess()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(IFoodRed, IFoodRedDark)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Logo
            Text(
                text = "PriPriDelivery",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = "Peça comida de qualquer lugar",
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Card de login
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Entrar",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Faça login para continuar",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Tabs Email / Telefone
                    Row(modifier = Modifier.fillMaxWidth()) {
                        FilterChip(
                            selected = metodoLogin == "email",
                            onClick = { metodoLogin = "email"; etapaOtp = 0 },
                            label = { Text("E-mail") },
                            modifier = Modifier.weight(1f).padding(end = 4.dp)
                        )
                        FilterChip(
                            selected = metodoLogin == "telefone",
                            onClick = { metodoLogin = "telefone"; etapaOtp = 0 },
                            label = { Text("Telefone") },
                            modifier = Modifier.weight(1f).padding(start = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (etapaOtp == 0) {
                        // Entrada de email ou telefone
                        if (metodoLogin == "email") {
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("E-mail") },
                                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                        } else {
                            OutlinedTextField(
                                value = telefone,
                                onValueChange = { telefone = it },
                                label = { Text("Telefone") },
                                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                // Simula envio de OTP e avança para etapa 1
                                etapaOtp = 1
                            },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = IFoodRed)
                        ) {
                            Text("Enviar código", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    } else {
                        // Entrada do código OTP
                        Text(
                            text = "Insira o código enviado para\n${if (metodoLogin == "email") email else telefone}",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = codigoOtp,
                            onValueChange = { if (it.length <= 6) codigoOtp = it.filter { c -> c.isDigit() } },
                            label = { Text("Código de verificação") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { authViewModel.loginAnonimo() },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = IFoodRed),
                            enabled = codigoOtp.length == 6
                        ) {
                            Text("Verificar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }

                        TextButton(onClick = { etapaOtp = 0 }) {
                            Text("Voltar", color = IFoodRed)
                        }
                    }

                    // Erro
                    uiState.erro?.let { erro ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = erro, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Divisor
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f))
                        Text(
                            "ou",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botão Google
                    OutlinedButton(
                        onClick = {
                            coroutineScope.launch {
                                try {

                                    val googleIdOption = GetGoogleIdOption.Builder()
                                        .setFilterByAuthorizedAccounts(false)
                                        .setServerClientId(webClientId)
                                        .setAutoSelectEnabled(false)
                                        .build()

                                    val request = GetCredentialRequest.Builder()
                                        .addCredentialOption(googleIdOption)
                                        .build()

                                    val result = CredentialManager.create(context)
                                        .getCredential(
                                            request = request,
                                            context = context as Activity
                                        )

                                    val credential = GoogleIdTokenCredential
                                        .createFrom(result.credential.data)

                                    authViewModel.loginComGoogle(
                                        credential.idToken
                                    )

                                } catch (e: GetCredentialException) {
                                    Log.e("GoogleLogin", "Erro Google", e)
                                } catch (e: Exception) {
                                    Log.e("GoogleLogin", "Erro geral", e)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Continuar com Google", fontWeight = FontWeight.Medium)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = { /* Facebook Sign-In */ },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Continuar com Facebook", fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Link para cadastro
            TextButton(onClick = onCadastroClick) {
                Text(
                    "Não tem conta? Cadastre-se",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        // Loading overlay
        if (uiState.carregando) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = IFoodRed)
            }
        }
    }
}
