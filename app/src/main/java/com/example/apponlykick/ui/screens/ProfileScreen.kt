package com.example.apponlykick.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.apponlykick.R
import com.example.apponlykick.data.model.Product

@Composable
fun ProfileScreen(
    userName: String,
    userEmail: String,
    profileImageUri: String?,
    onProfileImageChange: (String) -> Unit,
    favoriteProducts: List<Product>,
    onLogoutClick: () -> Unit,
    onProductClick: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit,
    onAddToCartClick: (Int) -> Unit,
    onPurchaseHistoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { onProfileImageChange(it.toString()) }
        }
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    AsyncImage(
                        model = profileImageUri ?: R.drawable.logo_onlykick,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .clickable { imagePickerLauncher.launch("image/*") },
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = userEmail,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.height(16.dp))
                    // 2. Botones de acción del perfil
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = onPurchaseHistoryClick) {
                            Text("MIS COMPRAS")
                        }
                        Button(onClick = onLogoutClick) {
                            Text("CERRAR SESIÓN")
                        }
                    }
                }
            }
            Spacer(Modifier.height(32.dp))
        }

        if (favoriteProducts.isNotEmpty()) {
            item {
                Text(
                    text = "Mis Favoritos",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }
            items(favoriteProducts) { product ->
                ProductCard(
                    product = product,
                    isFavorite = true,
                    onProductClick = { onProductClick(product.id) },
                    onFavoriteClick = { onFavoriteClick(product.id) },
                    onAddToCartClick = { onAddToCartClick(product.id) },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        } else {
            item {
                Text(
                    text = "Aún no tienes productos favoritos. ¡Explora la tienda y añade algunos!",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}