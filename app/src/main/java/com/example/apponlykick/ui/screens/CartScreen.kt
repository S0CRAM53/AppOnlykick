package com.example.apponlykick.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.apponlykick.data.model.Product

data class CartItemState(
    val product: Product,
    val quantity: Int
)

@Composable
fun CartScreen(
    cartItems: List<CartItemState>,
    totalPrice: String,
    onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit,
    onCheckoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (cartItems.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Tu carrito está vacío", style = MaterialTheme.typography.titleLarge)
        }
    } else {
        Column(modifier = modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(cartItems) { cartItem ->
                    CartItem(
                        product = cartItem.product,
                        quantity = cartItem.quantity,
                        onIncrease = { onIncrease(cartItem.product.id) },
                        onDecrease = { onDecrease(cartItem.product.id) }
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Total: $totalPrice",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onCheckoutClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Proceder al pago:)")
                }
            }
        }
    }
}