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
class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun profileScreen_displaysUserDataAndFavorites() {
        val favoriteProducts = listOf(
            Product(1, "Zapatillas de correr", 99, "", MarcaDto("Nike")),
            Product(2, "Botas de fútbol", 129, "", MarcaDto("Adidas"))
        )

        composeTestRule.setContent {
            ProfileScreen(
                userName = "Juan Carlos",
                userEmail = "juanki@gmail.com",
                profileImageUri = null,
                onProfileImageChange = {},
                favoriteProducts = favoriteProducts,
                onLogoutClick = {},
                onProductClick = {},
                onFavoriteClick = {},
                onAddToCartClick = {}
            )
        }

        composeTestRule.onNodeWithText("Juan Carlos").assertIsDisplayed()
        composeTestRule.onNodeWithText("juanki@gmail.com").assertIsDisplayed()
        composeTestRule.onNodeWithText("CERRAR SESIÓN").assertIsDisplayed()
        composeTestRule.onNodeWithText("Zapatillas de correr").assertIsDisplayed()
        composeTestRule.onNodeWithText("Botas de fútbol").assertIsDisplayed()
    }
}