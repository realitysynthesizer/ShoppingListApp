package com.example.shoppinglist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun LocationSelectionScreen(
    location : LocationData,
    onLocationSelected : (LocationData) -> Unit
) {
    val userLocation = remember{mutableStateOf(LatLng(location.latitude, location.longitude))}
    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(userLocation.value, 10f)
    }

    Column(modifier =  Modifier.fillMaxSize()) {
        GoogleMap(modifier = Modifier.weight(1f), cameraPositionState= cameraPositionState, onMapClick = {
            userLocation.value = it}){

            Marker(MarkerState(position = userLocation.value))
        }

        Button(onClick = {
            onLocationSelected(LocationData(userLocation.value.latitude, userLocation.value.longitude))
        }) {
            Text("Select Location")
        }
    }

}