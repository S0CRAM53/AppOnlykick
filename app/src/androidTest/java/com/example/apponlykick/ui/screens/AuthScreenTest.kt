package com.example.apponlykick.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.apponlykick.ui.viewmodel.OnlyKickUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun authScreen_displaysFieldsAndButtons() {
        composeTestRule.setContent {
            AuthScreen(
                uiState = OnlyKickUiState(),
                onNameChange = {},
                onEmailChange = {},
                onPassChange = {},
                onLoginClick = {},
                onRegisterClick = {}
            )
        }

        composeTestRule.onNodeWithText("Nombre").assertIsDisplayed()
        composeTestRule.onNodeWithText("Correo Electrónico").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()
        composeTestRule.onNodeWithText("Iniciar Sesion").assertIsDisplayed()
        composeTestRule.onNodeWithText("Registrate!").assertIsDisplayed()
    }
}