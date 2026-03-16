package com.splitsmart.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.splitsmart.app.ui.components.SplitSmartScaffold
import com.splitsmart.app.ui.navigation.Routes
import com.splitsmart.app.ui.navigation.SplitSmartNavGraph
import com.splitsmart.app.ui.screens.auth.AuthViewModel
import com.splitsmart.app.ui.theme.SplitSmartTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SplitSmartTheme {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    SplitSmartApp()
                }
            }
        }
    }
}

@Composable
fun SplitSmartApp() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.uiState.collectAsState()

    var isCheckingAuth by remember { mutableStateOf(true) }
    var isAuthenticated by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        authViewModel.checkAuth()
    }

    LaunchedEffect(authState.user, authState.isLoading) {
        if (!authState.isLoading) {
            isAuthenticated = authState.user != null
            isCheckingAuth = false
        }
    }

    if (isCheckingAuth) return

    val startDestination = if (isAuthenticated) Routes.DASHBOARD else Routes.LOGIN

    val currentRoute by navController.currentBackStackEntryFlow.collectAsState(initial = null)
    val route = currentRoute?.destination?.route
    val showBottomBar = route in listOf(Routes.DASHBOARD, Routes.GROUPS, Routes.FRIENDS, Routes.LEDGER, Routes.PROFILE)

    if (showBottomBar) {
        SplitSmartScaffold(navController = navController, currentRoute = route ?: Routes.DASHBOARD) {
            SplitSmartNavGraph(navController = navController, startDestination = startDestination)
        }
    } else {
        SplitSmartNavGraph(navController = navController, startDestination = startDestination)
    }
}
