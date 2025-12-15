package com.example.apponlykick.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.apponlykick.data.model.SaleResponse
import com.example.apponlykick.ui.viewmodel.OnlyKickUiState
import com.example.apponlykick.ui.viewmodel.OnlyKickViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseHistoryScreen(
    viewModel: OnlyKickViewModel,
    uiState: OnlyKickUiState,
    onBackClick: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.fetchSalesHistory()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Historial de Compras") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else if (uiState.salesHistory.isEmpty()) {
                Text("Aún no has realizado ninguna compra.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.salesHistory) { sale ->
                        SaleHistoryItem(sale = sale)
                    }
                }
            }
        }
    }
}

@Composable
private fun SaleHistoryItem(sale: SaleResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Pedido #${sale.idVenta}", fontWeight = FontWeight.Bold)
                Text(formatDate(sale.fechaVenta), style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Total: ${String.format("$%.2f", sale.totalVenta)}")
            Text("Estado: ${sale.estadoVenta.nombre}")
            Spacer(modifier = Modifier.height(16.dp))
            Text("Artículos:", fontWeight = FontWeight.Medium)
            sale.productosVenta.forEach {
                Text("  - ${it.cantidad}x ${it.producto.name} (${it.talla.nombre}, ${it.color.nombre})")
            }
        }
    }
}

private fun formatDate(dateString: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        parser.parse(dateString)?.let { formatter.format(it) } ?: dateString
    } catch (e: Exception) {
        dateString
    }
}
