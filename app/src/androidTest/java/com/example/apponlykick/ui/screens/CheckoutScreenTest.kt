package com.example.apponlykick.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CheckoutScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun checkoutScreen_displaysSuccessMessageAndButton() {
        composeTestRule.setContent {
            CheckoutScreen(onContinueShopping = {})
        }

        composeTestRule.onNodeWithText("¡Gracias por tu compra!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Seguir Comprando").assertIsDisplayed()
    }
}