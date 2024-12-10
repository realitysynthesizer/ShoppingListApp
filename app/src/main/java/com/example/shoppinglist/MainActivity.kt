package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.shoppinglist.ui.theme.ShoppingListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ShoppingListTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Navigation()

                }
            }
        }
    }
}

@Composable
fun Navigation(){
    val viewmodel:LocationViewModel = viewModel()
    val navController = rememberNavController()
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)

    NavHost(navController,"shoppinglistscreen" ) {
        composable("shoppinglistscreen") {
            ShoppingListApp(viewmodel, context, locationUtils, navController = navController, address = viewmodel.address.value.firstOrNull()?.formatted_address ?: "address not available")
        }
        dialog("locationscreen"){
            viewmodel.location.value?.let { it1 ->
                LocationSelectionScreen(it1) {
                    viewmodel.fetchAddress(latLng = "${it.latitude},${it.longitude}")
                    navController.popBackStack()
                }
            }
        }

    }
}



