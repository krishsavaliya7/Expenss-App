package com.splitsmart.app.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.splitsmart.app.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(uiState: DashboardUiState, onGroupClick: (Int) -> Unit, onNotificationsClick: () -> Unit, onAddExpense: () -> Unit, onRefresh: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
        LargeTopAppBar(title = { Row(verticalAlignment = Alignment.CenterVertically) {
            uiState.user?.let { AvatarCircle(it.fullName, 40, it.profilePicUrl) }
            Column(Modifier.padding(start = 12.dp)) { Text("Hello, ${uiState.user?.fullName?.split(" ")?.firstOrNull() ?: "there"}!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold) }
        }}, actions = { IconButton(onClick = onNotificationsClick) { BadgedBox(badge = { if (uiState.notificationCount > 0) Badge { Text(uiState.notificationCount.toString()) } }) { Icon(Icons.Filled.Notifications, "Notifications") } } }, scrollBehavior = scrollBehavior, colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = MaterialTheme.colorScheme.surface))
    }, floatingActionButton = { FloatingActionButton(onClick = onAddExpense, containerColor = MaterialTheme.colorScheme.primary) { Icon(Icons.Filled.Add, "Add Expense") } }) { padding ->
        if (uiState.isLoading) ShimmerList(modifier = Modifier.padding(padding))
        else LazyColumn(Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { Spacer(Modifier.height(4.dp)); BalanceSummaryRow(uiState.youOwe, uiState.youAreOwed) }
            item { Spacer(Modifier.height(8.dp)); Text("Your Groups", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold) }
            if (uiState.groups.isEmpty()) item { Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))) { Text("No groups yet. Create one to get started!", Modifier.padding(24.dp), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant) } }
            items(uiState.groups) { GroupCard(it, onClick = { onGroupClick(it.groupId) }) }
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}
