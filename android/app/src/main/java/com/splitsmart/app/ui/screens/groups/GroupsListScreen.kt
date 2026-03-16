package com.splitsmart.app.ui.screens.groups

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.splitsmart.app.ui.components.*
import com.splitsmart.app.ui.theme.*
import com.splitsmart.app.util.formatCurrency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupsListScreen(uiState: GroupUiState, onGroupClick: (Int) -> Unit, onCreateGroup: () -> Unit, onRefresh: () -> Unit) {
    Scaffold(topBar = { TopAppBar(title = { Text("Groups", fontWeight = FontWeight.Bold) }) }, floatingActionButton = { FloatingActionButton(onClick = onCreateGroup) { Icon(Icons.Filled.Add, "Create Group") } }) { padding ->
        if (uiState.isLoading) ShimmerList(modifier = Modifier.padding(padding))
        else if (uiState.groups.isEmpty()) EmptyState(Icons.Outlined.Groups, "No groups yet", "Create a group to start splitting expenses", Modifier.padding(padding))
        else LazyColumn(Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { Spacer(Modifier.height(4.dp)) }; items(uiState.groups) { GroupCard(it, onClick = { onGroupClick(it.groupId) }) }; item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailScreen(uiState: GroupUiState, groupId: Int, onAddExpense: () -> Unit, onSettlement: () -> Unit, onBack: () -> Unit, onDeleteExpense: (Int) -> Unit, onRefresh: () -> Unit) {
    var selectedTab by remember { mutableIntStateOf(0) }; val tabs = listOf("Expenses", "Balances", "Members"); val currency = uiState.currentGroup?.currency ?: "INR"
    Scaffold(topBar = { TopAppBar(title = { Text(uiState.currentGroup?.groupName ?: "Group", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }, actions = { TextButton(onClick = onSettlement) { Icon(Icons.Filled.Handshake, null, Modifier.padding(end = 4.dp)); Text("Settle") } }) }, floatingActionButton = { FloatingActionButton(onClick = onAddExpense) { Icon(Icons.Filled.Add, "Add Expense") } }) { padding ->
        Column(Modifier.padding(padding)) {
            PrimaryTabRow(selectedTabIndex = selectedTab) { tabs.forEachIndexed { i, t -> Tab(selectedTab == i, onClick = { selectedTab = i }, text = { Text(t) }) } }
            if (uiState.isLoading) ShimmerList()
            else when (selectedTab) {
                0 -> if (uiState.expenses.isEmpty()) EmptyState(Icons.Outlined.Receipt, "No expenses yet", "Add an expense to get started") else LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { items(uiState.expenses) { ExpenseItem(it, currency, onDelete = { onDeleteExpense(it.expenseId) }) }; item { Spacer(Modifier.height(80.dp)) } }
                1 -> LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { items(uiState.balances) { b -> Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) { Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Row(verticalAlignment = Alignment.CenterVertically) { AvatarCircle(b.fullName ?: b.username, 36); Text(b.fullName ?: b.username, Modifier.padding(start = 12.dp), style = MaterialTheme.typography.bodyLarge) }; Text((if (b.balance >= 0) "+" else "") + b.balance.formatCurrency(currency), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = if (b.balance >= 0) SuccessGreen else ErrorRed) } } } }
                2 -> LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { items(uiState.members) { m -> Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))) { Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) { AvatarCircle(m.fullName ?: m.username, 40, m.profilePicUrl); Column(Modifier.padding(start = 12.dp)) { Text(m.fullName ?: m.username, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold); Text("@${m.username}" + if (m.role == "admin") " (Admin)" else "", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) } } } } }
            }
        }
    }
}
