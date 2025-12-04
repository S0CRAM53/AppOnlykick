package com.example.apponlykick

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apponlykick.repository.SettingsRepository
import com.example.apponlykick.ui.screens.AppNavigation
import com.example.apponlykick.ui.theme.OnlyKickAppTheme
import com.example.apponlykick.ui.viewmodel.OnlyKickViewModel
import com.example.apponlykick.ui.viewmodel.OnlyKickViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val settingsRepository = SettingsRepository(context)
            val viewModel: OnlyKickViewModel = viewModel(
                factory = OnlyKickViewModelFactory(settingsRepository)
            )
            val uiState by viewModel.uiState.collectAsState()

            OnlyKickAppTheme(darkTheme = uiState.isDarkModeEnabled) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(viewModel = viewModel, uiState = uiState)
                }
            }
        }
    }
}