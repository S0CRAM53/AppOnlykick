package com.example.apponlykick.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.apponlykick.data.model.MarcaDto
import com.example.apponlykick.data.model.Product
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_displaysTopBarAndProductList() {
        val productList = listOf(
            Product(1, "Zapatillas de correr", 99, "", MarcaDto("Nike")),
            Product(2, "Botas de fútbol", 129, "", MarcaDto("Adidas"))
        )

        composeTestRule.setContent {
            HomeScreen(
                productList = productList,
                favoriteProductIds = emptySet(),
                isLoading = false,
                errorMessage = null,
                onProductClick = {},
                onFavoriteClick = {},
                onAddToCartClick = {}
            )
        }

        composeTestRule.onNodeWithText("Zapatillas de correr").assertIsDisplayed()
        composeTestRule.onNodeWithText("Botas de fútbol").assertIsDisplayed()
    }
}