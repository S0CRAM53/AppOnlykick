package com.example.apponlykick.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.apponlykick.data.model.MarcaDto
import com.example.apponlykick.data.model.Product
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.text.NumberFormat

@RunWith(AndroidJUnit4::class)
class ProductDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun productDetailScreen_displaysProductDetailsAndActions() {
        val product = Product(1, "Zapatillas de correr", 99, "", MarcaDto("Nike"))

        composeTestRule.setContent {
            ProductDetailScreen(
                product = product,
                isFavorite = false,
                onFavoriteClick = {},
                onAddToCartClick = {},
                onBackClick = {}
            )
        }

        composeTestRule.onAllNodesWithText("Zapatillas de correr")[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Zapatillas de correr")[1].assertIsDisplayed()
        composeTestRule.onNodeWithText("by Nike").assertIsDisplayed()
        val formattedPrice = NumberFormat.getCurrencyInstance().format(99)
        composeTestRule.onNodeWithText(formattedPrice).assertIsDisplayed()
        composeTestRule.onNodeWithText("Añadir a Favoritos").assertIsDisplayed()
        composeTestRule.onNodeWithText("Añadir al Carrito").assertIsDisplayed()
    }
}