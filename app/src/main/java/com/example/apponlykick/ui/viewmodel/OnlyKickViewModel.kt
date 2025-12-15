package com.example.apponlykick.ui.viewmodel

import android.icu.text.NumberFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.apponlykick.data.model.Product
import com.example.apponlykick.data.model.SaleResponse
import com.example.apponlykick.data.remote.ApiClient
import com.example.apponlykick.repository.AuthRepository
import com.example.apponlykick.repository.ProductRepository
import com.example.apponlykick.repository.SaleRepository
import com.example.apponlykick.repository.SettingsRepository
import com.example.apponlykick.ui.screens.CartItemState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class User(
    val id: String,
    val email: String,
    val name: String
)

sealed class AuthResult {
    data class Success(val user: User) : AuthResult()
    data class Error(val message: String) : AuthResult()
    object Loading : AuthResult()
    object Idle : AuthResult()
}

data class OnlyKickUiState(
    val productList: List<Product> = emptyList(),
    val favoriteProducts: Set<Int> = emptySet(),
    val cartProducts: Map<Int, Int> = emptyMap(),
    val currentUser: User? = null,
    val selectedProduct: Product? = null,
    val authResult: AuthResult = AuthResult.Idle,
    val isDarkModeEnabled: Boolean = false,
    val profileImageUri: String? = null,

    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val name: String = "",
    val email: String = "",
    val pass: String = "",
    val nameError: String? = null,
    val emailError: String? = null,
    val passError: String? = null,

    val salesHistory: List<SaleResponse> = emptyList(),
    val isCheckingOut: Boolean = false,
    val checkoutResult: Boolean? = null
) {
    val cartItems: List<CartItemState> by lazy {
        cartProducts.mapNotNull { (productId, quantity) ->
            val product = productList.find { it.id == productId }
            product?.let { CartItemState(product = it, quantity = quantity) }
        }
    }
    val totalCartPrice: Double by lazy { cartItems.sumOf { (it.product.price * it.quantity) } }
    fun getFormattedTotalPrice(): String { return NumberFormat.getCurrencyInstance().format(totalCartPrice) }

    val favoriteProductList: List<Product> by lazy {
        productList.filter { product ->
            favoriteProducts.contains(product.id)
        }
    }
}

class OnlyKickViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(OnlyKickUiState())
    val uiState: StateFlow<OnlyKickUiState> = _uiState.asStateFlow()

    private val authRepository = AuthRepository()
    private val productRepository = ProductRepository(ApiClient.service)
    private val saleRepository = SaleRepository()

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val productsFromRepo = productRepository.getProducts()
                _uiState.update { it.copy(productList = productsFromRepo, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al cargar productos: ${e.localizedMessage ?: "Desconocido"}"
                    )
                }
            }
        }

        settingsRepository.isDarkModeEnabled
            .onEach { isEnabled ->
                _uiState.update { it.copy(isDarkModeEnabled = isEnabled) }
            }
            .launchIn(viewModelScope)
    }

    fun confirmCheckout() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val user = currentState.currentUser
            val cartItems = currentState.cartItems

            if (user == null || cartItems.isEmpty()) {
                _uiState.update { it.copy(checkoutResult = false, errorMessage = "Usuario no autenticado o carrito vacío") }
                return@launch
            }

            _uiState.update { it.copy(isCheckingOut = true, errorMessage = null) }

            try {
                val result = saleRepository.createSale(user, cartItems, currentState.totalCartPrice)
                if (result != null) {
                    _uiState.update {
                        it.copy(
                            isCheckingOut = false,
                            checkoutResult = true,
                            cartProducts = emptyMap()
                        )
                    }
                    fetchSalesHistory()
                } else {
                    _uiState.update {
                        it.copy(
                            isCheckingOut = false,
                            checkoutResult = false,
                            errorMessage = "Error al procesar la compra."
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isCheckingOut = false,
                        checkoutResult = false,
                        errorMessage = "Error: ${e.message}"
                    )
                }
            }
        }
    }

    fun fetchSalesHistory() {
        viewModelScope.launch {
            val user = _uiState.value.currentUser
            if (user != null) {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                try {
                    val history = saleRepository.getSalesHistory(user.id.toInt())
                    _uiState.update { it.copy(salesHistory = history ?: emptyList(), isLoading = false) }
                } catch (e: Exception) {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Error al cargar el historial: ${e.message}") }
                }
            }
        }
    }

    fun resetCheckoutResult() {
        _uiState.update { it.copy(checkoutResult = null) }
    }

    fun onNameChange(name: String) { _uiState.update { it.copy(name = name, nameError = null) } }
    fun onEmailChange(email: String) { _uiState.update { it.copy(email = email, emailError = null) } }
    fun onPassChange(pass: String) { _uiState.update { it.copy(pass = pass, passError = null) } }

    private fun validateFields(isLogin: Boolean = false): Boolean {
        var nameError: String? = null
        var emailError: String? = null
        var passError: String? = null

        if (!isLogin) {
            if (_uiState.value.name.isBlank()) {
                nameError = "El nombre es obligatorio"
            }
        }

        if (_uiState.value.email.isBlank()) {
            emailError = "El correo es obligatorio"
        } else if (!_uiState.value.email.contains("@") || !_uiState.value.email.endsWith(".com")) {
            emailError = "El formato de correo es inválido"
        }

        if (_uiState.value.pass.isBlank()) {
            passError = "La contraseña es obligatoria"
        } else if (_uiState.value.pass.length < 6) {
            passError = "La contraseña debe tener al menos 6 caracteres"
        }

        _uiState.update { it.copy(nameError = nameError, emailError = emailError, passError = passError) }

        return nameError == null && emailError == null && passError == null
    }

    fun register() {
        if (validateFields()) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                val newUser = authRepository.registerUser(_uiState.value.name, _uiState.value.email, _uiState.value.pass)

                if (newUser != null) {
                    _uiState.update { it.copy(currentUser = newUser, isLoading = false) }
                } else {
                    _uiState.update {
                        it.copy(
                            emailError = "El usuario ya existe o hubo un error de red",
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun login() {
        if (validateFields(isLogin = true)) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                val user = authRepository.loginUser(_uiState.value.email, _uiState.value.pass)

                if (user != null) {
                    _uiState.update { it.copy(currentUser = user, isLoading = false) }
                    fetchSalesHistory() // Cargar historial al iniciar sesión
                } else {
                    _uiState.update {
                        it.copy(
                            emailError = "Credenciales incorrectas",
                            passError = "Credenciales incorrectas",
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun onProfileImageChange(uri: String) {
        _uiState.update { it.copy(profileImageUri = uri) }
    }

    fun logout() {
        _uiState.update {
            it.copy(
                currentUser = null,
                cartProducts = emptyMap(),
                favoriteProducts = emptySet(),
                profileImageUri = null,
                salesHistory = emptyList()
            )
        }
    }

    fun resetAuthResult() {
        _uiState.update { it.copy(authResult = AuthResult.Idle) }
    }

    fun toggleFavorite(productId: Int) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val updatedFavorites = currentState.favoriteProducts.toMutableSet()
                if (updatedFavorites.contains(productId)) {
                    updatedFavorites.remove(productId)
                } else {
                    updatedFavorites.add(productId)
                }
                currentState.copy(favoriteProducts = updatedFavorites)
            }
        }
    }

    fun addToCart(productId: Int) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val updatedCart = currentState.cartProducts.toMutableMap()
                val currentQuantity = updatedCart.getOrDefault(productId, 0)
                updatedCart[productId] = currentQuantity + 1
                currentState.copy(cartProducts = updatedCart)
            }
        }
    }

    fun removeFromCart(productId: Int) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val updatedCart = currentState.cartProducts.toMutableMap()
                val currentQuantity = updatedCart.getOrDefault(productId, 0)
                if (currentQuantity > 1) {
                    updatedCart[productId] = currentQuantity - 1
                } else {
                    updatedCart.remove(productId)
                }
                currentState.copy(cartProducts = updatedCart)
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(cartProducts = emptyMap())
            }
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            settingsRepository.setDarkMode(!_uiState.value.isDarkModeEnabled)
        }
    }

    fun selectProduct(productId: Int) {
        val product = _uiState.value.productList.find { it.id == productId }
        _uiState.update { it.copy(selectedProduct = product) }
    }
}

class OnlyKickViewModelFactory(private val settingsRepository: SettingsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OnlyKickViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OnlyKickViewModel(settingsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
