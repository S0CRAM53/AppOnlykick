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
class CartScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun cartScreen_displaysCartItemsAndTotal() {
        val cartItems = listOf(
            CartItemState(Product(1, "Zapatillas de correr", 99, "", MarcaDto("Nike")), 2),
            CartItemState(Product(2, "Botas de fútbol", 129, "", MarcaDto("Adidas")), 1)
        )

        composeTestRule.setContent {
            CartScreen(
                cartItems = cartItems,
                totalPrice = "$329.97",
                onIncrease = {},
                onDecrease = {},
                onCheckoutClick = {}
            )
        }

        composeTestRule.onNodeWithText("Zapatillas de correr").assertIsDisplayed()
        composeTestRule.onNodeWithText("Botas de fútbol").assertIsDisplayed()
        composeTestRule.onNodeWithText("Total: $329.97").assertIsDisplayed()
    }
}