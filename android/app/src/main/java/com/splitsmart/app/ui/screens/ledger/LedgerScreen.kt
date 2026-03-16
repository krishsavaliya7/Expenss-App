package com.splitsmart.app.ui.screens.ledger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitsmart.app.data.api.models.GroupData
import com.splitsmart.app.data.api.models.TransactionData
import com.splitsmart.app.data.repository.GroupRepository
import com.splitsmart.app.data.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.splitsmart.app.ui.components.*
import com.splitsmart.app.ui.theme.SuccessGreen
import com.splitsmart.app.util.formatCurrency
import com.splitsmart.app.util.formatDateTime

data class LedgerUiState(val isLoading: Boolean = false, val transactions: List<TransactionData> = emptyList(), val groups: List<GroupData> = emptyList(), val selectedGroupId: Int? = null, val error: String? = null)

@HiltViewModel
class LedgerViewModel @Inject constructor(private val groupRepository: GroupRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(LedgerUiState()); val uiState: StateFlow<LedgerUiState> = _uiState.asStateFlow()
    init { loadGroups() }
    fun loadGroups() { viewModelScope.launch { when (val r = groupRepository.getGroups()) { is Result.Success -> { val groups = r.data.groups ?: emptyList(); _uiState.value = _uiState.value.copy(groups = groups); if (groups.isNotEmpty()) loadTransactions(groups.first().groupId) }; else -> {} } } }
    fun loadTransactions(groupId: Int) { viewModelScope.launch { _uiState.value = _uiState.value.copy(isLoading = true, selectedGroupId = groupId); when (val r = groupRepository.getTransactions(groupId)) { is Result.Success -> _uiState.value = _uiState.value.copy(isLoading = false, transactions = r.data.transactions ?: emptyList()); is Result.Error -> _uiState.value = _uiState.value.copy(isLoading = false, error = r.message); else -> {} } } }
    fun selectGroup(groupId: Int) { loadTransactions(groupId) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LedgerScreen(uiState: LedgerUiState, onSelectGroup: (Int) -> Unit, onRefresh: () -> Unit) {
    Scaffold(topBar = { TopAppBar(title = { Text("Ledger", fontWeight = FontWeight.Bold) }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            if (uiState.groups.isNotEmpty()) {
                Row(Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    uiState.groups.forEach { g -> FilterChip(selected = uiState.selectedGroupId == g.groupId, onClick = { onSelectGroup(g.groupId) }, label = { Text(g.groupName) }) }
                }
            }
            if (uiState.isLoading) ShimmerList()
            else if (uiState.transactions.isEmpty()) EmptyState(Icons.Outlined.Receipt, "No transactions", "Transactions will appear here")
            else LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.transactions) { tx ->
                    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                        Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) { AvatarCircle(tx.fromUser, 28); Text(" → ", style = MaterialTheme.typography.bodyMedium); AvatarCircle(tx.toUser, 28) }
                                Spacer(Modifier.height(4.dp))
                                Text("${tx.paymentMethod ?: "split"} • ${tx.createdAt.formatDateTime()}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(tx.amount.formatCurrency("INR"), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                if (tx.txHash != null) Icon(Icons.Filled.Verified, "Verified", Modifier.size(16.dp), tint = SuccessGreen)
                            }
                        }
                    }
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}
