package com.pripridelivery.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapaEndereco(
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier
) {
    val posicao = LatLng(latitude, longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(posicao, 15f)
    }

    GoogleMap(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = posicao),
            title = "Endereço selecionado"
        )
    }
}
