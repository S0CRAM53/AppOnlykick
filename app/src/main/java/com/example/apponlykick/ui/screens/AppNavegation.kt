package com.example.apponlykick.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.apponlykick.ui.viewmodel.OnlyKickUiState
import com.example.apponlykick.ui.viewmodel.OnlyKickViewModel


enum class Screen {
    Home,
    Profile,
    Settings,
    Cart,
    Checkout,
    Detail,
    PurchaseHistory
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val screen: Screen
)

@Composable
fun AppNavigation(
    viewModel: OnlyKickViewModel,
    uiState: OnlyKickUiState,
    modifier: Modifier = Modifier
) {
    var showSplashScreen by remember { mutableStateOf(true) }

    if (showSplashScreen) {
        SplashScreen(onTimeout = { showSplashScreen = false })
    } else {
        if (uiState.currentUser == null) {
            AuthScreen(
                uiState = uiState,
                onNameChange = { viewModel.onNameChange(it) },
                onEmailChange = { viewModel.onEmailChange(it) },
                onPassChange = { viewModel.onPassChange(it) },
                onLoginClick = { viewModel.login() },
                onRegisterClick = { viewModel.register() }
            )
        } else {
            var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

            if (uiState.selectedProduct != null) {
                ProductDetailScreen(
                    product = uiState.selectedProduct,
                    isFavorite = uiState.favoriteProducts.contains(uiState.selectedProduct.id),
                    onFavoriteClick = { viewModel.toggleFavorite(uiState.selectedProduct.id) },
                    onAddToCartClick = { viewModel.addToCart(uiState.selectedProduct.id) },
                    onBackClick = { viewModel.selectProduct(0) }
                )
            } else {
                Scaffold(
                    topBar = {
                        if (currentScreen != Screen.Checkout && currentScreen != Screen.PurchaseHistory) {
                            val totalItemsInCart = uiState.cartProducts.values.sum()
                            AppTopBar(
                                cartItemCount = totalItemsInCart,
                                onCartClick = { currentScreen = Screen.Cart }
                            )
                        }
                    },
                    bottomBar = {
                        if (currentScreen != Screen.Detail && currentScreen != Screen.Checkout && currentScreen != Screen.PurchaseHistory) {
                            val navItems = listOf(
                                BottomNavItem("Inicio", Icons.Default.Home, Screen.Home),
                                BottomNavItem("Perfil", Icons.Default.Person, Screen.Profile),
                                BottomNavItem("Ajustes", Icons.Default.Settings, Screen.Settings)
                            )
                            NavigationBar {
                                navItems.forEach { navItem ->
                                    val isSelected = currentScreen == navItem.screen
                                    NavigationBarItem(
                                        selected = isSelected,
                                        onClick = { currentScreen = navItem.screen },
                                        icon = { Icon(imageVector = navItem.icon, contentDescription = navItem.label) },
                                        label = { Text(navItem.label) }
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    AnimatedContent(targetState = currentScreen, label = "ScreenAnimation") { screen ->
                        when (screen) {
                            Screen.Home -> {
                                HomeScreen(
                                    productList = uiState.productList,
                                    favoriteProductIds = uiState.favoriteProducts,
                                    isLoading = uiState.isLoading,
                                    errorMessage = uiState.errorMessage,
                                    onProductClick = { productId -> viewModel.selectProduct(productId) },
                                    onFavoriteClick = { productId -> viewModel.toggleFavorite(productId) },
                                    onAddToCartClick = { productId -> viewModel.addToCart(productId) },
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            Screen.Cart -> {
                                CartScreen(
                                    cartItems = uiState.cartItems,
                                    totalPrice = uiState.getFormattedTotalPrice(),
                                    onIncrease = { productId -> viewModel.addToCart(productId) },
                                    onDecrease = { productId -> viewModel.removeFromCart(productId) },
                                    onCheckoutClick = { currentScreen = Screen.Checkout },
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            Screen.Profile -> {
                                ProfileScreen(
                                    userName = uiState.currentUser!!.name,
                                    userEmail = uiState.currentUser!!.email,
                                    profileImageUri = uiState.profileImageUri,
                                    onProfileImageChange = { viewModel.onProfileImageChange(it) },
                                    favoriteProducts = uiState.favoriteProductList,
                                    onLogoutClick = { viewModel.logout() },
                                    onProductClick = { productId -> viewModel.selectProduct(productId) },
                                    onFavoriteClick = { productId -> viewModel.toggleFavorite(productId) },
                                    onAddToCartClick = { productId -> viewModel.addToCart(productId) },
                                    onPurchaseHistoryClick = { currentScreen = Screen.PurchaseHistory },
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            Screen.Settings -> SettingsScreen(
                                isDarkMode = uiState.isDarkModeEnabled,
                                onThemeToggle = { viewModel.toggleTheme() },
                                modifier = Modifier.padding(innerPadding)
                            )
                            Screen.Checkout -> {
                                CheckoutScreen(
                                    viewModel = viewModel,
                                    uiState = uiState,
                                    onPurchaseSuccess = { currentScreen = Screen.PurchaseHistory }
                                )
                            }
                            Screen.PurchaseHistory -> {
                                PurchaseHistoryScreen(
                                    viewModel = viewModel,
                                    uiState = uiState,
                                    onBackClick = { currentScreen = Screen.Profile } // Acción de volver
                                )
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}