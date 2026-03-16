package com.splitsmart.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController

data class BottomNavItem(val route: String, val label: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector)

val bottomNavItems = listOf(
    BottomNavItem("dashboard", "Home", Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavItem("groups", "Groups", Icons.Filled.Groups, Icons.Outlined.Groups),
    BottomNavItem("friends", "Friends", Icons.Filled.People, Icons.Outlined.People),
    BottomNavItem("ledger", "Ledger", Icons.Filled.MenuBook, Icons.Outlined.MenuBook),
    BottomNavItem("profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person)
)

@Composable
fun SplitSmartScaffold(navController: NavHostController, currentRoute: String, content: @Composable () -> Unit) {
    Scaffold(bottomBar = {
        NavigationBar {
            bottomNavItems.forEach { item ->
                val selected = currentRoute == item.route
                NavigationBarItem(selected = selected, onClick = { if (!selected) navController.navigate(item.route) { popUpTo("dashboard") { saveState = true }; launchSingleTop = true; restoreState = true } },
                    icon = { Icon(if (selected) item.selectedIcon else item.unselectedIcon, item.label) },
                    label = { Text(item.label) })
            }
        }
    }) { padding ->
        Box(Modifier.padding(padding)) { content() }
    }
}
