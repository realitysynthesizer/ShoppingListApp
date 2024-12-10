package com.example.shoppinglist

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LocationViewModel: ViewModel() {
    private val _location = mutableStateOf<LocationData?>(null)
    val location : State<LocationData?> = _location

    private val _address = mutableStateOf(listOf<GeocodingResult>())
    val address : State<List<GeocodingResult>> = _address


    fun updateLocation(newLocation: LocationData){
        _location.value = newLocation
    }

    fun fetchAddress(latLng: String){
        try {
            viewModelScope.launch {
                val response = RetrofitClient.create().getAddressFromCoordinates(latLng, "AIzaSyCUTGqBEQ_8I7iaPgMTL_DhODqZic-rdyQ")
                _address.value = response.results
            }
        }
        catch (e:Exception){
            Log.d("res1", "${e.cause} ${e.message}")
        }
    }
}