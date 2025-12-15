package com.example.apponlykick.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.apponlykick.ui.viewmodel.OnlyKickUiState
import com.example.apponlykick.ui.viewmodel.OnlyKickViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    viewModel: OnlyKickViewModel,
    uiState: OnlyKickUiState,
    onPurchaseSuccess: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(uiState.checkoutResult) {
        when (uiState.checkoutResult) {
            true -> {
                Toast.makeText(context, "¡Compra realizada con éxito!", Toast.LENGTH_LONG).show()
                onPurchaseSuccess()
                viewModel.resetCheckoutResult()
            }
            false -> { // Fallo
                val errorMessage = uiState.errorMessage ?: "Error al procesar la compra."
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                viewModel.resetCheckoutResult()
            }
            null -> {
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Confirmar Compra") })
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Total: ${uiState.getFormattedTotalPrice()}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { viewModel.confirmCheckout() },
                        enabled = !uiState.isCheckingOut && uiState.cartItems.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (uiState.isCheckingOut) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Confirmar y Pagar")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.cartItems.isEmpty() && !uiState.isCheckingOut) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay nada que pagar.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Text(
                            "Resumen del Pedido",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    items(uiState.cartItems) { cartItem ->
                        CartItemRow(item = cartItem)
                    }
                }
            }
        }
    }
}

@Composable
private fun CartItemRow(item: CartItemState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("${item.quantity}x ${item.product.name}")
        Text(String.format("$%.2f", item.product.price * item.quantity))
    }
}
