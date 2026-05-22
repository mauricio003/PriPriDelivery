package com.pripridelivery.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pripridelivery.ui.theme.IFoodRed

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = IFoodRed)
            Spacer(modifier = Modifier.height(16.dp))
            Text("PriPriDelivery", style = MaterialTheme.typography.headlineLarge)
        }
    }
}
