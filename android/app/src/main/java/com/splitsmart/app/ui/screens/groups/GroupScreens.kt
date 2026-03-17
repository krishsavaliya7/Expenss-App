package com.splitsmart.app.ui.screens.groups

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Handshake
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.splitsmart.app.data.api.models.CreateExpenseRequest
import com.splitsmart.app.data.api.models.SettlementData
import com.splitsmart.app.ui.components.*
import com.splitsmart.app.util.Constants
import com.splitsmart.app.util.formatCurrency

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CreateGroupScreen(uiState: GroupUiState, onCreateGroup: (String, String?, String, List<String>?) -> Unit, onBack: () -> Unit, onGroupCreated: (Int) -> Unit) {
    var groupName by remember { mutableStateOf("") }; var description by remember { mutableStateOf("") }; var selectedCurrency by remember { mutableStateOf("INR") }; var currencyExpanded by remember { mutableStateOf(false) }; val selectedMembers = remember { mutableStateListOf<String>() }; var memberInput by remember { mutableStateOf("") }
    LaunchedEffect(uiState.createdGroupId) { uiState.createdGroupId?.let { onGroupCreated(it) } }
    Scaffold(topBar = { TopAppBar(title = { Text("Create Group", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(groupName, { groupName = it }, label = { Text("Group Name") }, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp))
            OutlinedTextField(description, { description = it }, label = { Text("Description (optional)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), minLines = 2, maxLines = 3)
            ExposedDropdownMenuBox(currencyExpanded, { currencyExpanded = it }) { OutlinedTextField("${Constants.currencySymbol(selectedCurrency)} $selectedCurrency", {}, readOnly = true, label = { Text("Currency") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(currencyExpanded) }, modifier = Modifier.fillMaxWidth().menuAnchor(), shape = RoundedCornerShape(16.dp)); ExposedDropdownMenu(currencyExpanded, { currencyExpanded = false }) { Constants.CURRENCIES.forEach { (code, sym) -> DropdownMenuItem(text = { Text("$sym $code") }, onClick = { selectedCurrency = code; currencyExpanded = false }) } } }
            OutlinedTextField(memberInput, { memberInput = it }, label = { Text("Add Members (username)") }, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), trailingIcon = { if (memberInput.isNotBlank()) IconButton(onClick = { if (memberInput.isNotBlank() && memberInput !in selectedMembers) { selectedMembers.add(memberInput.trim()); memberInput = "" } }) { Text("Add", color = MaterialTheme.colorScheme.primary) } })
            if (selectedMembers.isNotEmpty()) FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) { selectedMembers.forEach { m -> AssistChip(onClick = { selectedMembers.remove(m) }, label = { Text(m) }, trailingIcon = { Icon(Icons.Filled.Close, "Remove", Modifier.size(16.dp)) }) } }
            AnimatedVisibility(uiState.error != null) { Text(uiState.error ?: "", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) }
            Spacer(Modifier.height(8.dp))
            Button(onClick = { onCreateGroup(groupName, description.ifBlank { null }, selectedCurrency, selectedMembers.ifEmpty { null }) }, Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp), enabled = !uiState.isLoading && groupName.isNotBlank()) { if (uiState.isLoading) CircularProgressIndicator(Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp) else Text("Create Group", style = MaterialTheme.typography.titleMedium) }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(uiState: GroupUiState, groupId: Int, onCreateExpense: (CreateExpenseRequest) -> Unit, onBack: () -> Unit, onExpenseCreated: () -> Unit) {
    var name by remember { mutableStateOf("") }; var amount by remember { mutableStateOf("") }; var selectedCategory by remember { mutableStateOf("Other") }; var categoryExpanded by remember { mutableStateOf(false) }; var splitType by remember { mutableStateOf("equal") }; var paidByExpanded by remember { mutableStateOf(false) }; var paidBy by remember { mutableStateOf("") }
    LaunchedEffect(uiState.members) { if (paidBy.isBlank() && uiState.members.isNotEmpty()) paidBy = uiState.members.firstOrNull()?.username ?: "" }
    LaunchedEffect(uiState.expenseCreated) { if (uiState.expenseCreated) onExpenseCreated() }
    Scaffold(topBar = { TopAppBar(title = { Text("Add Expense", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(amount, { if (it.all { c -> c.isDigit() || c == '.' }) amount = it }, label = { Text("Amount") }, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), textStyle = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), prefix = { Text(Constants.currencySymbol(uiState.currentGroup?.currency ?: "INR")) })
            OutlinedTextField(name, { name = it }, label = { Text("Expense Name") }, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp))
            ExposedDropdownMenuBox(categoryExpanded, { categoryExpanded = it }) { OutlinedTextField(selectedCategory, {}, readOnly = true, label = { Text("Category") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(categoryExpanded) }, modifier = Modifier.fillMaxWidth().menuAnchor(), shape = RoundedCornerShape(16.dp)); ExposedDropdownMenu(categoryExpanded, { categoryExpanded = false }) { Constants.EXPENSE_CATEGORIES.forEach { DropdownMenuItem(text = { Text(it) }, onClick = { selectedCategory = it; categoryExpanded = false }) } } }
            ExposedDropdownMenuBox(paidByExpanded, { paidByExpanded = it }) { OutlinedTextField(paidBy, {}, readOnly = true, label = { Text("Paid By") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(paidByExpanded) }, modifier = Modifier.fillMaxWidth().menuAnchor(), shape = RoundedCornerShape(16.dp)); ExposedDropdownMenu(paidByExpanded, { paidByExpanded = false }) { uiState.members.forEach { m -> DropdownMenuItem(text = { Text(m.fullName ?: m.username) }, onClick = { paidBy = m.username; paidByExpanded = false }) } } }
            Text("Split Type", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) { Constants.SPLIT_TYPES.forEachIndexed { i, t -> SegmentedButton(splitType == t, { splitType = t }, SegmentedButtonDefaults.itemShape(i, Constants.SPLIT_TYPES.size)) { Text(t.replaceFirstChar { it.uppercase() }) } } }
            Spacer(Modifier.height(8.dp))
            Button(onClick = { val a = amount.toDoubleOrNull() ?: 0.0; if (name.isNotBlank() && a > 0 && paidBy.isNotBlank()) onCreateExpense(CreateExpenseRequest(name, a, paidBy, splitType, selectedCategory)) }, Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp), enabled = !uiState.isLoading && name.isNotBlank() && amount.isNotBlank()) { if (uiState.isLoading) CircularProgressIndicator(Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp) else Text("Add Expense", style = MaterialTheme.typography.titleMedium) }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettlementScreen(uiState: GroupUiState, groupId: Int, onRequestCash: (String, Double) -> Unit, onInitiateUpi: (String, Double) -> Unit, onBack: () -> Unit) {
    val currency = uiState.currentGroup?.currency ?: "INR"
    Scaffold(topBar = { TopAppBar(title = { Text("Settlements", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }) }) { padding ->
        if (uiState.isLoading) ShimmerList(modifier = Modifier.padding(padding))
        else if (uiState.settlements.isEmpty()) EmptyState(Icons.Outlined.Handshake, "All settled up!", "No pending settlements", Modifier.padding(padding))
        else LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { Text("Suggested Settlements", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold) }
            items(uiState.settlements) { s -> Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) { Row(verticalAlignment = Alignment.CenterVertically) { AvatarCircle(s.from, 36); Text(" pays ", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(horizontal = 4.dp)); AvatarCircle(s.to, 36) }; Text(s.amount.formatCurrency(currency), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) { FilledTonalButton(onClick = { onRequestCash(s.to, s.amount) }, Modifier.weight(1f)) { Icon(Icons.Filled.Payments, null, Modifier.padding(end = 4.dp)); Text("Cash") }; FilledTonalButton(onClick = { onInitiateUpi(s.to, s.amount) }, Modifier.weight(1f)) { Icon(Icons.Filled.AccountBalance, null, Modifier.padding(end = 4.dp)); Text("UPI") } }
            } } }; item { Spacer(Modifier.height(24.dp)) }
        }
    }
}
